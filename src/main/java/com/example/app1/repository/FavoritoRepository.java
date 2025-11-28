package com.example.app1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app1.model.Usuario;
import com.example.app1.model.Favorito;
import com.example.app1.model.Restaurante;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    List<Favorito> findByUsuario(Usuario usuario);
    
    boolean existsByUsuarioAndRestaurante(Usuario usuario, Restaurante restaurante);

    Optional<Favorito> findByUsuarioAndRestaurante(Usuario usuario, Restaurante restaurante);

    void deleteByUsuarioAndRestaurante(Usuario usuario, Restaurante restaurante);
    
    void deleteByRestaurante(Restaurante restaurante);
}