package com.example.app1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app1.model.Usuario;
import com.example.app1.model.Favorito;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    List<Favorito> findByUsuario(Usuario usuario);
}
