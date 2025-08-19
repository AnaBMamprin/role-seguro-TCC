package com.example.app1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app1.model.Restaurante;
import com.example.app1.records.RestauranteDTO;
import com.example.app1.repository.RestauranteRepository;

import jakarta.transaction.Transactional;

@Service
public class RestauranteService {
	
	@Autowired
	RestauranteRepository repo;
	
	@Transactional
	public void converteRestaurantes( RestauranteDTO restauranteDTO ) {
		Restaurante restaurante = new Restaurante();
				restaurante.setNome(restauranteDTO.getNome());
				restaurante.setCidade(restauranteDTO.getCidade());
				restaurante.setCulinaria(restaurante.getCulinaria());
				restaurante.setTipodecardapio(restauranteDTO.getTipodeCardapio());
				repo.save(restaurante);
	}
	

}
