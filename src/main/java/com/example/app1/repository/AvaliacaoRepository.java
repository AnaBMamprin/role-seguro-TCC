package com.example.app1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app1.model.Avaliacao;
import com.example.app1.model.Usuario;

public interface AvaliacaoRepository extends JpaRepository <Avaliacao, Long> {
	
	List<Avaliacao> findTop5ByOrderByDataAvaliacaoDesc();
	
	boolean existsByUsuario_IdUsuarioAndRestaurante_Id(Long usuarioId, Long restauranteId);
	
	
}
