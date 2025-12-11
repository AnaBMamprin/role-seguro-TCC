package com.example.app1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate; 

import com.example.app1.model.Restaurante;
import com.example.app1.model.Usuario;
import com.example.app1.records.RestauranteDTO;
import com.example.app1.repository.AvaliacaoRepository;
import com.example.app1.repository.RestauranteRepository;
import com.example.app1.repository.UserRepository;
import com.example.app1.usuarioEnums.UserEnum;
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

        if (dto.getRua() != null && !dto.getRua().isEmpty()) {
            
            System.out.println("[SERVICE ATUALIZAR] Novo endereço detectado (Rua: " + dto.getRua() + "). Buscando novas coordenadas...");

            String enderecoParaSalvar = dto.getRua() + ", " + dto.getNumero();
            
            String enderecoParaGoogle = String.format(
                "%s, %s, %s, %s, %s",
                dto.getRua(),
                dto.getNumero(),
                dto.getBairro(),
                dto.getCidade(),
                dto.getEstado()
            );

            restaurante.setEndereco(enderecoParaSalvar);
            restaurante.setCidade(dto.getCidade());
            restaurante.setEstado(dto.getEstado());
            restaurante.setBairro(dto.getBairro());
            restaurante.setCep(dto.getCep());
            
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

        Restaurante restaurante = new Restaurante();

        restaurante.setNome(dto.getNome());
        restaurante.setCulinaria(dto.getCulinaria());
        restaurante.setHorario(dto.getHorario());
        restaurante.setSite(dto.getSite());
        restaurante.setTipodeprato(dto.getTipodeprato());
        restaurante.setCaminhoFoto(dto.getCaminhoFoto());
        restaurante.setTelefone(dto.getTelefone());
        
        restaurante.setEndereco(enderecoCompletoParaSalvar);
        restaurante.setCidade(dto.getCidade());
        restaurante.setEstado(dto.getEstado());

        restaurante.setLatitude(latitude);
        restaurante.setLongitude(longitude);
        

        repo.save(restaurante);
    }
    
    @Transactional
	public void converteRestaurantes( RestauranteDTO dto ) {
    	
    	System.out.println("--- [SERVICE DATA-LOADER] INICIANDO CONVERSÃO ---");
		
        double latitude = 0.0;
        double longitude = 0.0;
        String enderecoParaSalvar = "";
        
        try {
            String enderecoCompletoParaGoogle = String.format(
                "%s, %s, %s, %s, %s",
                dto.getRua(),
                dto.getNumero(),
                dto.getBairro(),
                dto.getCidade(),
                dto.getEstado()
            );
            
            enderecoParaSalvar = dto.getRua() + ", " + dto.getNumero();

            System.out.println("[SERVICE DATA-LOADER] Buscando coordenadas para: " + enderecoCompletoParaGoogle);
            
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
                System.err.println("Erro de Geocoding (DataLoader): " + status.asText());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Falha ao chamar a API de Geocoding (DataLoader).");
        }

		Restaurante restaurante = new Restaurante();

        restaurante.setNome(dto.getNome());
        restaurante.setCidade(dto.getCidade());
        restaurante.setEstado(dto.getEstado());
        restaurante.setCulinaria(dto.getCulinaria()); 
        restaurante.setTipodeprato(dto.getTipodeprato());
        restaurante.setHorario(dto.getHorario());
        restaurante.setEndereco(enderecoParaSalvar);
        restaurante.setSite(dto.getSite());
        restaurante.setTelefone(dto.getTelefone());
        restaurante.setCep(dto.getCep());
        restaurante.setBairro(dto.getBairro());
        restaurante.setLatitude(latitude);
        restaurante.setLongitude(longitude);

        repo.save(restaurante);
	}
    
    public Double getMediaDeAvaliacoes(Long restauranteId) {
        return avaliacaoRepository.getMediaAvaliacoes(restauranteId);
    }
    
    public String getGoogleMapsApiKey() {
        return this.apiKey;
    }
    
}