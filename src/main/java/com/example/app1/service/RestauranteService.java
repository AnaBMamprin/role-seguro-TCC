package com.example.app1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate; 

import com.example.app1.model.Restaurante;
import com.example.app1.records.RestauranteDTO;
import com.example.app1.repository.RestauranteRepository;
import com.fasterxml.jackson.databind.JsonNode; 
import com.fasterxml.jackson.databind.ObjectMapper; 

import jakarta.transaction.Transactional;
import java.net.URLEncoder; 
import java.nio.charset.StandardCharsets; 

@Service
public class RestauranteService {
	
	@Autowired
	RestauranteRepository repo;

    @Value("${google.maps.api.key}")
    private String apiKey;
	
	private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ========================================================================
    // SEU MÉTODO DE CADASTRO (QUE JÁ FIZEMOS - ESTÁ CORRETO)
    // ========================================================================
	@Transactional
	public void converteRestaurantes( RestauranteDTO restauranteDTO ) {
		
        double latitude = 0.0;
        double longitude = 0.0;

        try {
            String endereco = restauranteDTO.getEndereco();
            String enderecoFormatado = URLEncoder.encode(endereco, StandardCharsets.UTF_8);
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
        
		Restaurante restaurante = new Restaurante();
        restaurante.setNome(restauranteDTO.getNome());
        restaurante.setCidade(restauranteDTO.getCidade());
        restaurante.setCulinaria(restauranteDTO.getCulinaria()); // Correção do bug anterior
        restaurante.setTipodeprato(restauranteDTO.getTipodeprato());
        restaurante.setHorario(restauranteDTO.getHorario());
        restaurante.setEndereco(restauranteDTO.getEndereco());
        restaurante.setSite(restauranteDTO.getSite());
        restaurante.setLatitude(latitude);
        restaurante.setLongitude(longitude);
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

        // ==============================================================
        // VERIFICA SE O ENDEREÇO MUDOU ANTES DE FAZER GEOCODING
        // ==============================================================
        if (!restaurante.getEndereco().equals(dto.getEndereco())) {
            // O endereço mudou! Precisamos buscar novas coordenadas.
            restaurante.setEndereco(dto.getEndereco()); // Atualiza o endereço

            try {
                String enderecoFormatado = URLEncoder.encode(dto.getEndereco(), StandardCharsets.UTF_8);
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
                    restaurante.setLatitude(0.0);
                    restaurante.setLongitude(0.0);
                }
            } catch (Exception e) {
                e.printStackTrace();
                restaurante.setLatitude(0.0);
                restaurante.setLongitude(0.0);
            }
        }
        // Se o endereço NÃO mudou, as coordenadas antigas são mantidas.

        // Salva o restaurante atualizado
        repo.save(restaurante);
    }
}