package com.example.app1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate; 

import com.example.app1.model.Restaurante;
import com.example.app1.model.Usuario;
import com.example.app1.records.RestauranteDTO;
import com.example.app1.repository.RestauranteRepository;
import com.example.app1.repository.UserRepository;
import com.example.app1.usuarioEnums.UserEnum;
import com.fasterxml.jackson.databind.JsonNode; 
import com.fasterxml.jackson.databind.ObjectMapper; 

import jakarta.transaction.Transactional;
import java.net.URLEncoder; 
import java.nio.charset.StandardCharsets; 

@Service
public class RestauranteService {
	
	@Autowired
	RestauranteRepository repo;
	@Autowired
	private PasswordEncoder passwordEncoder; // Você DEVE ter isso
	@Autowired
	private UserRepository usuarioRepository;

    @Value("${google.maps.api.key}")
    private String apiKey;
	
	private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ========================================================================
    // SEU MÉTODO DE CADASTRO (QUE JÁ FIZEMOS - ESTÁ CORRETO)
    // ========================================================================
    @Transactional
	public void converteRestaurantes( RestauranteDTO restauranteDTO ) {
    	
    	System.out.println("--- [SERVICE] INICIANDO CONVERSÃO ---");
        // PONTO CRÍTICO 3: O DTO chegou no service?
        System.out.println("[SERVICE] DTO recebido. Foto no DTO: " + restauranteDTO.getCaminhoFoto());
		
        double latitude = 0.0;
        double longitude = 0.0;
        
        // (Lógica do Geocoding - está correta)
        try {
        	String enderecoCompleto = String.format(
        	        "%s, %s, %s, %s",
        	        restauranteDTO.getEndereco(), 
        	        restauranteDTO.getCidade(),   
        	        restauranteDTO.getEstado(),  
        	        "Brasil"                     
        	    );
            String enderecoFormatado = URLEncoder.encode(enderecoCompleto, StandardCharsets.UTF_8);
            String url = "https://maps.googleapis.com/maps/api/geocode/json" +
                         "?address=" + enderecoFormatado +
                         "&key=" + apiKey;

            String jsonResponse = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode status = root.path("status");

            if (status.asText().equals("OK")) {
                JsonNode location = root.path("results").get(0).path("geometry").path("location");
                latitude = location.path("lat").asDouble();
                longitude = location.path("lng").asDouble();
            } else {
                System.err.println("Erro de Geocoding: " + status.asText());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Falha ao chamar a API de Geocoding.");
        }
        
        // 1. Cria o objeto
		Restaurante restaurante = new Restaurante();
        
        // 2. Seta todos os dados do DTO
        restaurante.setNome(restauranteDTO.getNome());
        restaurante.setCidade(restauranteDTO.getCidade());
        restaurante.setCulinaria(restauranteDTO.getCulinaria()); 
        restaurante.setTipodeprato(restauranteDTO.getTipodeprato());
        restaurante.setHorario(restauranteDTO.getHorario());
        restaurante.setEndereco(restauranteDTO.getEndereco());
        restaurante.setSite(restauranteDTO.getSite());
        
        // 3. Seta as coordenadas
        restaurante.setLatitude(latitude);
        restaurante.setLongitude(longitude);
        
        if (restauranteDTO.getCaminhoFoto() != null && !restauranteDTO.getCaminhoFoto().isEmpty()) {
            restaurante.setCaminhoFoto(restauranteDTO.getCaminhoFoto());
        }
        
        // 5. Salva no banco
        repo.save(restaurante);
	}
	
    // ========================================================================
    // PARTE 1: ADICIONE ESTE NOVO MÉTODO DE ATUALIZAÇÃO
    // ========================================================================
    @Transactional
    public void atualizarRestaurante(Long id, RestauranteDTO dto) {
        
        // Dica: Usar orElseThrow é mais seguro para garantir que o restaurante existe
        Restaurante restaurante = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado com ID: " + id));

        // 1. Atualiza os campos normais
        restaurante.setNome(dto.getNome());
        // (Não atualize a cidade aqui, ela deve ser atualizada JUNTO com o endereço)
        // restaurante.setCidade(dto.getCidade()); 
        restaurante.setCulinaria(dto.getCulinaria());
        restaurante.setTipodeprato(dto.getTipodeprato());
        restaurante.setHorario(dto.getHorario());
        restaurante.setSite(dto.getSite());
        
        // 2. Atualiza a foto (se foi enviada)
        if (dto.getCaminhoFoto() != null && !dto.getCaminhoFoto().isEmpty()) {
            // (Aqui você poderia deletar a foto antiga antes de setar a nova)
            restaurante.setCaminhoFoto(dto.getCaminhoFoto());
        }

        // ==============================================================
        // LÓGICA DE ATUALIZAÇÃO DE ENDEREÇO CORRIGIDA
        // ==============================================================

        // 3. Verificamos se o usuário preencheu um NOVO endereço (olhando a RUA)
        if (dto.getRua() != null && !dto.getRua().isEmpty()) {
            
            System.out.println("[SERVICE ATUALIZAR] Novo endereço detectado (Rua: " + dto.getRua() + "). Buscando novas coordenadas...");

            // 4. Construímos os novos endereços
            String enderecoParaSalvar = dto.getRua() + ", " + dto.getNumero();
            
            String enderecoParaGoogle = String.format(
                "%s, %s, %s, %s, %s",
                dto.getRua(),
                dto.getNumero(),
                dto.getBairro(),
                dto.getCidade(), // Cidade nova do DTO
                dto.getEstado()  // Estado novo do DTO
            );

            // 5. Atualizamos os dados do restaurante
            restaurante.setEndereco(enderecoParaSalvar); // Ex: "Rua Augusta, 123"
            restaurante.setCidade(dto.getCidade());
            restaurante.setEstado(dto.getEstado());
            restaurante.setBairro(dto.getBairro());
            restaurante.setCep(dto.getCep());
            // (Se sua entidade não tem campos separados, tudo bem, 
            // mas setEndereco, setCidade e setEstado é o mínimo)

            // 6. Buscamos as novas coordenadas no Google Maps
            try {
                String enderecoFormatado = URLEncoder.encode(enderecoParaGoogle, StandardCharsets.UTF_8);
                String url = "https://maps.googleapis.com/maps/api/geocode/json" +
                             "?address=" + enderecoFormatado +
                             "&key=" + apiKey;

                System.out.println("[GOOGLE API] Enviando endereço para atualização: " + enderecoParaGoogle);

                String jsonResponse = restTemplate.getForObject(url, String.class);
                JsonNode root = objectMapper.readTree(jsonResponse);
                JsonNode status = root.path("status");

                if (status.asText().equals("OK")) {
                    JsonNode location = root.path("results").get(0).path("geometry").path("location");
                    restaurante.setLatitude(location.path("lat").asDouble());
                    restaurante.setLongitude(location.path("lng").asDouble());
                    System.out.println("[GOOGLE API] SUCESSO! Novas Coordenadas: " + restaurante.getLatitude() + ", " + restaurante.getLongitude());
                } else {
                    System.out.println("[GOOGLE API] FALHA! Status: " + status.asText());
                    restaurante.setLatitude(0.0);
                    restaurante.setLongitude(0.0);
                }
            } catch (Exception e) {
                System.out.println("[GOOGLE API] ERRO GRAVE! Exceção: " + e.getMessage());
                e.printStackTrace();
                restaurante.setLatitude(0.0);
                restaurante.setLongitude(0.0);
            }
            
        } else {
            // Se a 'rua' veio vazia, significa que o usuário NÃO quis alterar o endereço.
            // Não fazemos nada e mantemos os dados antigos.
            System.out.println("[SERVICE ATUALIZAR] Campo 'rua' vazio. Endereço antigo mantido.");
        }
        
        // 7. Salva o restaurante (com ou sem o endereço novo)
        repo.save(restaurante);
    }
    
    @Transactional
    public void salvarRestaurante(RestauranteDTO dto, Long idDoUsuarioDono) {

        // 1. BUSCAR O DONO (O Usuário JÁ DEVE EXISTIR)
        // Não vamos mais criar um 'new Usuario()'
        Usuario dono = usuarioRepository.findById(idDoUsuarioDono)
                .orElseThrow(() -> new RuntimeException("Usuário 'Dono' não encontrado com ID: " + idDoUsuarioDono));

        // 2. BUSCAR COORDENADAS (LÓGICA DO GEOCODING CORRIGIDA)
        double latitude = 0.0;
        double longitude = 0.0;
        String enderecoCompletoParaSalvar = "";

        try {
            // CORREÇÃO: Usar os campos separados do DTO
            String enderecoCompletoParaGoogle = String.format(
                "%s, %s, %s, %s, %s",
                dto.getRua(),       // "Avenida Estados Unidos"
                dto.getNumero(),    // "479"
                dto.getBairro(),    // "Jardim das Nações"
                dto.getCidade(),    // "Guarulhos"
                dto.getEstado()     // "SP"
            );
            
            // Vamos salvar o endereço no banco de forma legível
            enderecoCompletoParaSalvar = dto.getRua() + ", " + dto.getNumero();

            System.out.println("[SERVICE salvarRestaurante] Buscando coordenadas para: " + enderecoCompletoParaGoogle);
            
            String enderecoFormatado = URLEncoder.encode(enderecoCompletoParaGoogle, StandardCharsets.UTF_8);
            String url = "https://maps.googleapis.com/maps/api/geocode/json" +
                         "?address=" + enderecoFormatado +
                         "&key=" + apiKey;

            String jsonResponse = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode status = root.path("status");

            if (status.asText().equals("OK")) {
                JsonNode location = root.path("results").get(0).path("geometry").path("location");
                latitude = location.path("lat").asDouble();
                longitude = location.path("lng").asDouble();
                System.out.println("[GOOGLE API] SUCESSO! Lat: " + latitude + ", Lng: " + longitude);
            } else {
                System.err.println("Erro de Geocoding: " + status.asText());
            }
        } catch (Exception e) {
            System.err.println("Falha ao chamar a API de Geocoding em salvarRestaurante.");
            e.printStackTrace();
        }

        // 3. CRIAR O NOVO RESTAURANTE
        Restaurante restaurante = new Restaurante();

        // 4. PREENCHER OS DADOS DO RESTAURANTE (NENHUM DADO DE USUÁRIO AQUI)
        restaurante.setNome(dto.getNome());
        restaurante.setCulinaria(dto.getCulinaria());
        restaurante.setHorario(dto.getHorario());
        restaurante.setSite(dto.getSite());
        restaurante.setTipodeprato(dto.getTipodeprato());
        restaurante.setCaminhoFoto(dto.getCaminhoFoto());
        
        // Dados do endereço (corrigidos)
        restaurante.setEndereco(enderecoCompletoParaSalvar); // Salva "Avenida Estados Unidos, 479"
        restaurante.setCidade(dto.getCidade());
        restaurante.setEstado(dto.getEstado());
        // (Seu Model precisa ter Bairro, se você quiser salvar)
        // restaurante.setBairro(dto.getBairro()); 
        
        // Dados do Geocoding
        restaurante.setLatitude(latitude);
        restaurante.setLongitude(longitude);

        // 5. ATRELAR O DONO AO RESTAURANTE (A MÁGICA DO @ManyToOne)
        restaurante.setUsuario(dono);
        
        // 6. SALVAR
        // O JPA vai salvar o restaurante e preencher a chave estrangeira 'usuario_id'
        repo.save(restaurante);
    }
    
    public String getGoogleMapsApiKey() {
        return this.apiKey;
    }
    
}