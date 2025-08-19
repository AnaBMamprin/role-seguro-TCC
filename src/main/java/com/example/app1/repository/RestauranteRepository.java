package com.example.app1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app1.model.Restaurante;

public interface RestauranteRepository extends JpaRepository <Restaurante, Long > {
	 
	 Optional<Restaurante> findByNome(String nome);
	 List<Restaurante> findByCulinaria(String culinaria);

}
