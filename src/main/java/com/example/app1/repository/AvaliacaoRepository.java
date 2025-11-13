package com.example.app1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.app1.model.Avaliacao;
import com.example.app1.model.Usuario;

public interface AvaliacaoRepository extends JpaRepository <Avaliacao, Long> {
	
	List<Avaliacao> findTop5ByOrderByDataAvaliacaoDesc();
	
	boolean existsByUsuario_IdUsuarioAndRestaurante_Id(Long usuarioId, Long restauranteId);
	
	List<Avaliacao> findByRestauranteIdOrderByDataAvaliacaoDesc(Long restauranteId);
	
	@Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.restaurante.id = :restauranteId")
	Double getMediaAvaliacoes(@Param("restauranteId") Long restauranteId);
	
	Long countByRestauranteId(Long restauranteId);
	
	List<Avaliacao> findTop6ByOrderByDataAvaliacaoDesc();
}
