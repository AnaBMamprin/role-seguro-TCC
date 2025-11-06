package com.example.app1.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.app1.model.Avaliacao;
import com.example.app1.model.Restaurante;
import com.example.app1.model.Usuario;
import com.example.app1.records.RestauranteDTO;
import com.example.app1.repository.AvaliacaoRepository;
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
    
    @Autowired
    AvaliacaoRepository avaliacaoRepository;
    
    @Value("${google.maps.api.key}")
    private String apiKey;
    
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
         List<Avaliacao> avaliacoesRecentes = avaliacaoRepository.findTop5ByOrderByDataAvaliacaoDesc(); // Exemplo
         model.addAttribute("avaliacoesRecentes", avaliacoesRecentes);

        return "inicial"; // O nome do seu template HTML
    }

    @GetMapping("/restaurantes")
    public String mostrarRestaurantes(
            @RequestParam(name = "culinaria", required = false) String culinaria,
            @RequestParam(name = "page", defaultValue = "0") int page, // Número da página (começa em 0)
            @RequestParam(name = "size", defaultValue = "9") int size, // Itens por página (ex: 9 para grid 3x3)
            @RequestParam(name = "sort", defaultValue = "nome") String sort, // Opcional: Campo de ordenação
            @RequestParam(name = "direction", defaultValue = "ASC") String direction, // Opcional: Direção
            Model model) {

        // Cria o objeto Pageable
         Sort sortOrder = Sort.by(Sort.Direction.fromString(direction), sort); // Se usar ordenação
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
    public String detalhesRestaurante(
            @RequestParam("id") Long id, // <-- @RequestParam pega o "?id=45"
            Model model
        ) {
        
        Optional<Restaurante> restauranteOpt = reposi.findById(id);

        if (restauranteOpt.isPresent()) {
            Restaurante restaurante = restauranteOpt.get();
            
            Long usuarioId = getCurrentUserId();
            boolean isFavorito = (usuarioId != null) && favoritoService.isFavorito(usuarioId, id);
            
            // O método que criamos (agora vai funcionar)
            boolean podeAvaliar = verificarSePodeAvaliar(usuarioId, id);
            
            model.addAttribute("restaurante", restaurante);
            model.addAttribute("isFavorito", isFavorito);
            model.addAttribute("googleMapsApiKey", service.getGoogleMapsApiKey()); // ou getGoogleMapsApiKey()
            model.addAttribute("podeAvaliar", podeAvaliar); 

            // A linha mais importante para o seu formulário de avaliação
            model.addAttribute("novaAvaliacao", new com.example.app1.records.AvaliacaoDTO());

            // Diz ao Spring para carregar o ARQUIVO "modelo-restaurante.html"
            return "modelo-restaurante"; 
        } else {
            // Se o ID não existir, volta para a lista
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
    private boolean verificarSePodeAvaliar(Long usuarioId, Long restauranteId) {
        // 1. Se o usuário não está logado, ele não pode avaliar.
        if (usuarioId == null) {
            return false;
        }
        
        // 2. Pergunta ao repositório se já existe uma avaliação com essa combinação.
        // (Vamos ter que criar este método no repositório no próximo passo).
        boolean jaAvaliou = avaliacaoRepository.existsByUsuario_IdUsuarioAndRestaurante_Id(usuarioId, restauranteId);
        
        // 3. Ele "pode avaliar" se ele "NÃO" ( ! ) "já avaliou".
        return !jaAvaliou;
    }
    
    public String getGoogleMapsApiKey() {
        return this.apiKey;
    }
}