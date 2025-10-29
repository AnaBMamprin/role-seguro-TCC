package com.example.app1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app1.model.Usuario;
import com.example.app1.model.Favorito;
import com.example.app1.model.Restaurante;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    List<Favorito> findByUsuario(Usuario usuario);
    
 // Verifica se já existe um favorito para este usuário E este restaurante
    boolean existsByUsuarioAndRestaurante(Usuario usuario, Restaurante restaurante);

    // Encontra um favorito específico (para deletar)
    Optional<Favorito> findByUsuarioAndRestaurante(Usuario usuario, Restaurante restaurante);

    // Deleta um favorito específico (alternativa mais direta)
    void deleteByUsuarioAndRestaurante(Usuario usuario, Restaurante restaurante);
}