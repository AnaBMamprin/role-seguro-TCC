package com.example.app1.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.app1.model.Restaurante;
import com.example.app1.model.Usuario;
import com.example.app1.records.RestauranteDTO;
import com.example.app1.repository.RestauranteRepository;
import com.example.app1.repository.UserRepository;
import com.example.app1.service.RestauranteService;
import com.example.app1.service.FavoritoService;

@Controller
public class RestauranteController {

	private final RestauranteRepository reposi;
    private final RestauranteService service;
    private final FavoritoService favoritoService;
    private final UserRepository userRepository;
    
    @Autowired // O @Autowired no construtor é opcional nas versões mais recentes do Spring, mas bom para clareza
    public RestauranteController(RestauranteRepository repository, 
                                 RestauranteService service, 
                                 FavoritoService favoritoService, // Adicionado
                                 UserRepository userRepository) { // Adicionado
    	this.reposi = repository;
        this.service = service;
        this.favoritoService = favoritoService; // Inicializa
        this.userRepository = userRepository;   // Inicializa
    }
    
    @GetMapping("/inicial")
    public String paginaInicial(Model model) {

        // --- Lógica de Culinárias ---
        List<String> culinariasDisponiveis = reposi.findDistinctCulinarias();
        model.addAttribute("culinarias", culinariasDisponiveis);

        // --- Lógica de Usuário ---
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
             Object principal = auth.getPrincipal();
             String email = null;
             if (principal instanceof UserDetails) {
                 email = ((UserDetails) principal).getUsername();
             } else if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
                 email = ((org.springframework.security.oauth2.core.user.OAuth2User) principal).getAttribute("email");
             }
             // Você pode querer adicionar o nome do usuário ao model também
             if (email != null) {
                  userRepository.findByEmailUsuario(email).ifPresent(usuario -> {
                      model.addAttribute("nomeUsuarioLogado", usuario.getNomeUsuario()); // Ajuste getNomeUsuario se necessário
                  });
             }
             // model.addAttribute("email", email); // Se precisar do email na view
        }
        
        // --- Lógica para Feed de Avaliações (Quando implementar) ---
        // List<Avaliacao> avaliacoesRecentes = avaliacaoRepository.findTop5ByOrderByReviewDateDesc(); // Exemplo
        // model.addAttribute("avaliacoesRecentes", avaliacoesRecentes);

        return "inicial"; // O nome do seu template HTML
    }

    @GetMapping("/restaurantes")
    public String mostrarRestaurantes(
            @RequestParam(name = "culinaria", required = false) String culinaria,
            @RequestParam(name = "page", defaultValue = "0") int page, // Número da página (começa em 0)
            @RequestParam(name = "size", defaultValue = "9") int size, // Itens por página (ex: 9 para grid 3x3)
            // @RequestParam(name = "sort", defaultValue = "nome") String sort, // Opcional: Campo de ordenação
            // @RequestParam(name = "direction", defaultValue = "ASC") String direction, // Opcional: Direção
            Model model) {

        // Cria o objeto Pageable
        // Sort sortOrder = Sort.by(Sort.Direction.fromString(direction), sort); // Se usar ordenação
        Pageable pageable = PageRequest.of(page, size); // PageRequest.of(page, size, sortOrder) se usar sort

        Page<Restaurante> paginaRestaurantes; // Usa Page<>

        if (culinaria != null && !culinaria.isEmpty()) {
            paginaRestaurantes = reposi.findByCulinaria(culinaria, pageable);
        } else {
            paginaRestaurantes = reposi.findAll(pageable);
        }

        // --- Lógica de Favoritos (não muda) ---
        Long usuarioId = getCurrentUserId(); 
        Set<Long> idsFavoritos = (usuarioId != null) ? favoritoService.getFavoritoIds(usuarioId) : Collections.emptySet();

        // Envia o OBJETO PAGE inteiro para o Thymeleaf
        model.addAttribute("paginaRestaurantes", paginaRestaurantes); 
        model.addAttribute("culinaria", culinaria);
        model.addAttribute("idsFavoritos", idsFavoritos); 

        return "restaurantes";
    }
    
    @GetMapping("/modelo-restaurante")
    public String detalhesRestaurante(@RequestParam("id") Long id, Model model) {
        Optional<Restaurante> restauranteOpt = reposi.findById(id);

        if (restauranteOpt.isPresent()) {
            Restaurante restaurante = restauranteOpt.get();
            
            // --- ADICIONAR LÓGICA DE FAVORITOS ---
            Long usuarioId = getCurrentUserId();
            boolean isFavorito = (usuarioId != null) && favoritoService.isFavorito(usuarioId, id);
            
            // --- Lógica para ${podeAvaliar} (Exemplo) ---
            // boolean podeAvaliar = verificarSePodeAvaliar(usuarioId, id); // Implementar esta lógica
            // model.addAttribute("podeAvaliar", podeAvaliar); 

            model.addAttribute("restaurante", restaurante);
            model.addAttribute("isFavorito", isFavorito); // Envia para o Thymeleaf

            return "modelo-restaurante";
        } else {
            return "redirect:/restaurantes";
        }
    }

    // ======================================================
    // COPIE O MÉTODO AUXILIAR PARA CÁ TAMBÉM
    // ======================================================
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