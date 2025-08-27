package com.example.app1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app1.model.Favorito;
import com.example.app1.model.Restaurante;
import com.example.app1.model.Usuario;
import com.example.app1.repository.FavoritoRepository;
import com.example.app1.repository.UserRepository;

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private UserRepository usuarioRepository;

    public List<Restaurante> listarFavoritos(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmailUsuario(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return favoritoRepository.findByUsuario(usuario)
                .stream()
                .map(Favorito::getRestaurante)
                .toList();
    }
}

