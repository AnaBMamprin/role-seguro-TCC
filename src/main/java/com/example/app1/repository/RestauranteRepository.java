package com.example.app1.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app1.model.Restaurante;

public interface RestauranteRepository extends JpaRepository <Restaurante, Long > {
	 
	 Optional<Restaurante> findByNome(String nome);
	 List<Restaurante> findByCulinaria(String culinaria);

	 /**
	     * Busca no banco de dados uma lista de todas as strings de "culinaria"
	     * que são únicas (DISTINCT) e que não sejam nulas ou vazias.
	     */
	    @Query("SELECT DISTINCT r.culinaria FROM Restaurante r WHERE r.culinaria IS NOT NULL AND r.culinaria != ''")
	    List<String> findDistinctCulinarias();
}
