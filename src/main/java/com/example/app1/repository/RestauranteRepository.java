package com.example.app1.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import com.example.app1.model.Restaurante;
import com.example.app1.model.Usuario;

public interface RestauranteRepository extends JpaRepository <Restaurante, Long > {
	 
	 Optional<Restaurante> findByNome(String nome);
	 Page<Restaurante> findAll(Pageable pageable); 

	 Page<Restaurante> findByCulinaria(String culinaria, Pageable pageable);
	 
	 Page<Restaurante> findByTipodepratoContaining(String tipoDePrato, Pageable pageable);
	 
	 Page<Restaurante> findByCulinariaAndTipodepratoContaining(String culinaria, String tipoDePrato, Pageable pageable);
	 
	 Page<Restaurante> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

	    @Query("SELECT DISTINCT r.culinaria FROM Restaurante r WHERE r.culinaria IS NOT NULL AND r.culinaria != ''")
	    List<String> findDistinctCulinarias();
	    
	    List<Restaurante> findTop6ByOrderByIdDesc();
}
