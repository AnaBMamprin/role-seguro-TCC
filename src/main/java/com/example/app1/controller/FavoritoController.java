package com.example.app1.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.app1.model.Restaurante;
import com.example.app1.model.Usuario;
import com.example.app1.repository.AvaliacaoRepository; 
import com.example.app1.service.RestauranteService; 
import com.example.app1.repository.UserRepository;
import com.example.app1.service.FavoritoService;

@Controller
public class FavoritoController {

	private final FavoritoService favoritoService;
    private final UserRepository userRepository;
    
    // CAMPOS NOVOS
    private final RestauranteService restauranteService;
    private final AvaliacaoRepository avaliacaoRepository;
    
    @Autowired
    public FavoritoController(FavoritoService favoritoService, 
                              UserRepository userRepository,
                              // INJEÇÕES NOVAS
                              RestauranteService restauranteService,
                              AvaliacaoRepository avaliacaoRepository) {
        this.favoritoService = favoritoService;
        this.userRepository = userRepository;
        // ATRIBUIÇÕES NOVAS
        this.restauranteService = restauranteService;
        this.avaliacaoRepository = avaliacaoRepository;
    }

    @GetMapping("/favoritos")
    public String listarFavoritos(Model model) {
        Long usuarioId = getCurrentUserId();
        if (usuarioId == null) {
            return "redirect:/login";
        }
        
        List<Restaurante> restaurantesFavoritos = favoritoService.listarFavoritosPorUsuarioId(usuarioId);

        List<Restaurante> restaurantesOrdenados = restaurantesFavoritos.stream()
            .sorted(Comparator.comparing(Restaurante::getNome))
            .collect(Collectors.toList());
        
        Map<Long, Double> mapaDeMedias = new HashMap<>();
        Map<Long, Long> mapaDeContagem = new HashMap<>();
        
        for (Restaurante r : restaurantesOrdenados) { // <-- MUDANÇA AQUI
            Long id = r.getId();
            mapaDeMedias.put(id, restauranteService.getMediaDeAvaliacoes(id)); 
            mapaDeContagem.put(id, avaliacaoRepository.countByRestauranteId(id));
        }

        model.addAttribute("favoritos", restaurantesOrdenados); // <-- MUDANÇA AQUI
        
        model.addAttribute("mapaDeMedias", mapaDeMedias);
        model.addAttribute("mapaDeContagem", mapaDeContagem);

        return "favoritos";
    }

    // ======================================================
    // O RESTO DO SEU CONTROLLER (Sem mudanças)
    // ======================================================

    @PostMapping("/favoritos/add")
    @ResponseBody
    public ResponseEntity<?> adicionarFavorito(@RequestParam("restauranteId") Long restauranteId) {
        Long usuarioId = getCurrentUserId();
        if (usuarioId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado.");
        }
        boolean success = favoritoService.addFavorito(usuarioId, restauranteId);
        if (success) {
            return ResponseEntity.ok().body("Favorito adicionado.");
        } else {
            return ResponseEntity.badRequest().body("Erro ao adicionar favorito.");
        }
    }

    @PostMapping("/favoritos/remove")
    @ResponseBody
    public ResponseEntity<?> removerFavorito(@RequestParam("restauranteId") Long restauranteId) {
        Long usuarioId = getCurrentUserId();
        if (usuarioId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado.");
        }
        boolean success = favoritoService.removeFavorito(usuarioId, restauranteId);
        if (success) {
            return ResponseEntity.ok().body("Favorito removido.");
        } else {
            return ResponseEntity.badRequest().body("Erro ao remover favorito.");
        }
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            String email;
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                email = ((UserDetails) principal).getUsername();
            } else if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
                 email = ((org.springframework.security.oauth2.core.user.OAuth2User) principal).getAttribute("email");
            }
             else {
                 return null; 
            }

            if (email != null) {
                Optional<Usuario> userOpt = userRepository.findByEmailUsuario(email); 
                return userOpt.map(Usuario::getIdUsuario).orElse(null);
            }
        }
        return null;
    }
}