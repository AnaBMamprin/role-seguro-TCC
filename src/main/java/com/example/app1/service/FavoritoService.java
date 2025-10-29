package com.example.app1.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app1.model.Favorito;
import com.example.app1.model.Restaurante;
import com.example.app1.model.Usuario;
import com.example.app1.repository.FavoritoRepository;
import com.example.app1.repository.UserRepository;
import com.example.app1.repository.RestauranteRepository;

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private UserRepository usuarioRepository;
    
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Transactional(readOnly = true) // Boa prática adicionar readOnly para buscas
    public List<Restaurante> listarFavoritosPorUsuarioId(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + usuarioId));

        return favoritoRepository.findByUsuario(usuario)
                .stream()
                .map(Favorito::getRestaurante)
                .toList();
    }

    // ======================================================
    // MÉTODO PARA ADICIONAR FAVORITO
    // ======================================================
    @Transactional
    public boolean addFavorito(Long usuarioId, Long restauranteId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElse(null);

        if (usuario != null && restaurante != null) {
            // Verifica se já não é favorito
            if (!favoritoRepository.existsByUsuarioAndRestaurante(usuario, restaurante)) {
                Favorito novoFavorito = new Favorito();
                novoFavorito.setUsuario(usuario);
                novoFavorito.setRestaurante(restaurante);
                favoritoRepository.save(novoFavorito);
                return true;
            }
            // Já era favorito, não faz nada mas retorna sucesso (ou false se preferir)
            return true; 
        }
        return false; // Usuário ou Restaurante não encontrado
    }

    // ======================================================
    // MÉTODO PARA REMOVER FAVORITO
    // ======================================================
    @Transactional
    public boolean removeFavorito(Long usuarioId, Long restauranteId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElse(null);

        if (usuario != null && restaurante != null) {
            // Deleta diretamente usando o método do repositório
            favoritoRepository.deleteByUsuarioAndRestaurante(usuario, restaurante);
            // Ou, alternativamente:
            // Optional<Favorito> favOpt = favoritoRepository.findByUsuarioAndRestaurante(usuario, restaurante);
            // favOpt.ifPresent(favoritoRepository::delete);
            return true;
        }
        return false;
    }

    // ======================================================
    // MÉTODO PARA VERIFICAR SE É FAVORITO (para o front-end)
    // ======================================================
    @Transactional(readOnly = true)
    public boolean isFavorito(Long usuarioId, Long restauranteId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElse(null);

        if (usuario != null && restaurante != null) {
            return favoritoRepository.existsByUsuarioAndRestaurante(usuario, restaurante);
        }
        return false;
    }

    // ======================================================
    // MÉTODO PARA PEGAR IDs DOS FAVORITOS (para o front-end)
    // ======================================================
    @Transactional(readOnly = true)
    public Set<Long> getFavoritoIds(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario != null) {
            return favoritoRepository.findByUsuario(usuario)
                    .stream()
                    .map(favorito -> favorito.getRestaurante().getId()) // Pega o ID do Restaurante
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}