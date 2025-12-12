package com.example.app1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate; 

import com.example.app1.model.Restaurante;
import com.example.app1.records.RestauranteDTO;
import com.example.app1.repository.AvaliacaoRepository;
import com.example.app1.repository.CupomRepository;
import com.example.app1.repository.FavoritoRepository;
import com.example.app1.repository.RestauranteRepository;
import com.example.app1.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode; 
import com.fasterxml.jackson.databind.ObjectMapper; 

import org.springframework.transaction.annotation.Transactional;
import java.net.URLEncoder; 
import java.nio.charset.StandardCharsets; 

@Service
public class RestauranteService {
	
	@Autowired
	RestauranteRepository repo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository usuarioRepository;
	@Autowired 
    private AvaliacaoRepository avaliacaoRepository;
	@Autowired
    private FavoritoRepository favoritoRepository;
	@Autowired
    private CupomRepository cupomRepository;

    @Value("${google.maps.api.key}")
    private String apiKey;
	
	private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void atualizarRestaurante(Long id, RestauranteDTO dto) {
        
        Restaurante restaurante = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado com ID: " + id));

        restaurante.setNome(dto.getNome());
        restaurante.setCulinaria(dto.getCulinaria());
        restaurante.setTipodeprato(dto.getTipodeprato());
        restaurante.setHorario(dto.getHorario());
        restaurante.setSite(dto.getSite());
        restaurante.setTelefone(dto.getTelefone());
        
        if (dto.getCaminhoFoto() != null && !dto.getCaminhoFoto().isEmpty()) {
            restaurante.setCaminhoFoto(dto.getCaminhoFoto());
        }

        // Se o usuário digitou um endereço novo (Rua não está vazia)
        if (dto.getRua() != null && !dto.getRua().isEmpty()) {
            
            System.out.println("[SERVICE ATUALIZAR] Novo endereço detectado. Atualizando dados...");

            // 1. ATUALIZA AS COLUNAS NO BANCO (Isso estava faltando!)
            restaurante.setRua(dto.getRua());       // <--- Faltava
            restaurante.setNumero(dto.getNumero()); // <--- Faltava
            restaurante.setBairro(dto.getBairro());
            restaurante.setCep(dto.getCep());
            restaurante.setCidade(dto.getCidade());
            restaurante.setEstado(dto.getEstado());

            // 2. Monta string única para exibição
            String enderecoParaSalvar = dto.getRua() + ", " + dto.getNumero();
            restaurante.setEndereco(enderecoParaSalvar);
            
            // 3. Monta string para o Google
            String enderecoParaGoogle = String.format(
                "%s, %s, %s, %s, %s",
                dto.getRua(),
                dto.getNumero(),
                dto.getBairro(),
                dto.getCidade(),
                dto.getEstado()
            );

            // 4. Chama o Google Maps
            try {
                String enderecoFormatado = URLEncoder.encode(enderecoParaGoogle, StandardCharsets.UTF_8);
                String url = "https://maps.googleapis.com/maps/api/geocode/json" +
                             "?address=" + enderecoFormatado +
                             "&key=" + apiKey;

                String jsonResponse = restTemplate.getForObject(url, String.class);
                JsonNode root = objectMapper.readTree(jsonResponse);
                JsonNode status = root.path("status");

                if (status.asText().equals("OK")) {
                    JsonNode location = root.path("results").get(0).path("geometry").path("location");
                    restaurante.setLatitude(location.path("lat").asDouble());
                    restaurante.setLongitude(location.path("lng").asDouble());
                } else {
                    System.out.println("[GOOGLE API] FALHA! Status: " + status.asText());
                    // Mantém coordenadas antigas ou zera, depende da sua regra
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        } else {
            System.out.println("[SERVICE ATUALIZAR] Campo 'rua' vazio. Endereço antigo mantido.");
        }
        
        repo.save(restaurante);
        repo.flush();
    }
    
    @Transactional
    public void salvarRestaurante(RestauranteDTO dto) {

        double latitude = 0.0;
        double longitude = 0.0;
        String enderecoCompletoParaSalvar = "";

        try {
            String enderecoCompletoParaGoogle = String.format(
                "%s, %s, %s, %s, %s",
                dto.getRua(),
                dto.getNumero(),
                dto.getBairro(),
                dto.getCidade(),
                dto.getEstado()
            );
            
            enderecoCompletoParaSalvar = dto.getRua() + ", " + dto.getNumero();

            // Busca coordenadas...
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Restaurante restaurante = new Restaurante();

        restaurante.setNome(dto.getNome());
        restaurante.setCulinaria(dto.getCulinaria());
        restaurante.setHorario(dto.getHorario());
        restaurante.setSite(dto.getSite());
        restaurante.setTipodeprato(dto.getTipodeprato());
        restaurante.setCaminhoFoto(dto.getCaminhoFoto());
        restaurante.setTelefone(dto.getTelefone());
        
        // --- AQUI ESTAVAM FALTANDO OS SETTERS DO ENDEREÇO INDIVIDUAL ---
        restaurante.setRua(dto.getRua());       // <--- Adicionado
        restaurante.setNumero(dto.getNumero()); // <--- Adicionado
        restaurante.setCep(dto.getCep());       
        restaurante.setBairro(dto.getBairro());
        restaurante.setCidade(dto.getCidade());
        restaurante.setEstado(dto.getEstado());
        // -------------------------------------------------------------
        
        restaurante.setEndereco(enderecoCompletoParaSalvar); // String concatenada

        restaurante.setLatitude(latitude);
        restaurante.setLongitude(longitude);
        

        repo.save(restaurante);
    }
    
    // ... (o método converteRestaurantes e getMedia ficam iguais) ...
    @Transactional
	public void converteRestaurantes( RestauranteDTO dto ) {
        // Mantenha o seu método converteRestaurantes original ou adicione os setRua/setNumero nele também se quiser que o DataLoader funcione direito
        // ...
    }
    
    public Double getMediaDeAvaliacoes(Long restauranteId) {
        return avaliacaoRepository.getMediaAvaliacoes(restauranteId);
    }
    
    public String getGoogleMapsApiKey() {
        return this.apiKey;
    }
    
}