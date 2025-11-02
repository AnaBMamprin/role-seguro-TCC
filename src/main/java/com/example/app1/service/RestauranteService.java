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
        Restaurante restaurante = repo.findById(id).orElse(null);
        if (restaurante == null) {
            // Lidar com o erro (restaurante não encontrado)
            return; 
        }

        // Atualiza os campos normais
        restaurante.setNome(dto.getNome());
        restaurante.setCidade(dto.getCidade());
        restaurante.setCulinaria(dto.getCulinaria());
        restaurante.setTipodeprato(dto.getTipodeprato());
        restaurante.setHorario(dto.getHorario());
        restaurante.setSite(dto.getSite());
        
        if (dto.getCaminhoFoto() != null && !dto.getCaminhoFoto().isEmpty()) {
            // (Aqui você poderia deletar a foto antiga antes de setar a nova)
            restaurante.setCaminhoFoto(dto.getCaminhoFoto());
        }

     // ==============================================================
     // VERIFICA SE O ENDEREÇO MUDOU ANTES DE FAZER GEOCODING (CORRIGIDO)
     // ==============================================================

        String enderecoNovo = dto.getEndereco();
        String cidadeNova = dto.getCidade();     // <-- Pegue a nova cidade
        String estadoNovo = dto.getEstado();   // <-- Pegue o novo estado
        String enderecoAntigo = restaurante.getEndereco();

     // SÓ executa o Geocoding se:
     // 1. O endereço novo NÃO for nulo ou vazio
     // 2. O endereço novo for DIFERENTE do endereço antigo
     if (enderecoNovo != null && !enderecoNovo.isEmpty() && !java.util.Objects.equals(enderecoAntigo, enderecoNovo)) {

         System.out.println("[SERVICE ATUALIZAR] Endereço mudou. Buscando novas coordenadas...");
         restaurante.setEndereco(enderecoNovo); 
         restaurante.setCidade(cidadeNova);     // <-- Faltava isso
         restaurante.setEstado(estadoNovo);
         try {
        	 
        	 String enderecoCompleto = String.format(
        	            "%s, %s, %s, %s",
        	            enderecoNovo,
        	            cidadeNova,
        	            estadoNovo,
        	            "Brasil"
        	        );
        	 
             String enderecoFormatado = URLEncoder.encode(enderecoCompleto, StandardCharsets.UTF_8);
             String url = "https://maps.googleapis.com/maps/api/geocode/json" +
                          "?address=" + enderecoFormatado +
                          "&key=" + apiKey;

             // --- LOG DE DEPURAÇÃO 1 ---
             // Veja qual endereço você está enviando
             System.out.println("[GOOGLE API] Enviando endereço: " + dto.getEndereco());
             // Veja a URL completa (CUIDADO: não exponha sua API key)
             // System.out.println("[GOOGLE API] URL: " + url); 

             String jsonResponse = restTemplate.getForObject(url, String.class);
             
             // --- LOG DE DEPURAÇÃO 2 ---
             // Veja a resposta COMPLETA do Google
             System.out.println("[GOOGLE API] Resposta JSON: " + jsonResponse);

             JsonNode root = objectMapper.readTree(jsonResponse);
             JsonNode status = root.path("status");

             if (status.asText().equals("OK")) {
                 JsonNode location = root.path("results").get(0).path("geometry").path("location");
                 restaurante.setLatitude(location.path("lat").asDouble());
                 restaurante.setLongitude(location.path("lng").asDouble());
                 
                 // --- LOG DE DEPURAÇÃO 3 ---
                 System.out.println("[GOOGLE API] SUCESSO! Lat: " + restaurante.getLatitude() + ", Lng: " + restaurante.getLongitude());
                 
             } else {
                 // --- LOG DE DEPURAÇÃO 4 ---
                 // O Google respondeu, mas com um status de erro
                 System.out.println("[GOOGLE API] FALHA! Status: " + status.asText());
                 
                 restaurante.setLatitude(0.0);
                 restaurante.setLongitude(0.0);
             }
         } catch (Exception e) {
             // --- LOG DE DEPURAÇÃO 5 ---
             // A chamada falhou ANTES de receber uma resposta
             System.out.println("[GOOGLE API] ERRO GRAVE! Exceção: " + e.getMessage());
             
             e.printStackTrace();
             restaurante.setLatitude(0.0);
             restaurante.setLongitude(0.0);
         }
     }
     // Se o endereço novo for igual ao antigo, ou se for vazio, 
     // ele simplesmente ignora este bloco e mantém os dados antigos.
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