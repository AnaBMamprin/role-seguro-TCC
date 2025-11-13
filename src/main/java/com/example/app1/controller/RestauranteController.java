package com.example.app1.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

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
        
        List<Avaliacao> avaliacoesRecentes = avaliacaoRepository.findTop6ByOrderByDataAvaliacaoDesc();
        model.addAttribute("avaliacoesRecentes", avaliacoesRecentes);
        
        List<Restaurante> restaurantesRecentes = reposi.findTop6ByOrderByIdDesc();
        model.addAttribute("restaurantesRecentes", restaurantesRecentes);
        
        List<Long> idsRecentes = restaurantesRecentes.stream()
                .map(Restaurante::getId)
                .collect(Collectors.toList());

		Map<Long, Double> mapaDeMedias = new HashMap<>();
		Map<Long, Long> mapaDeContagem = new HashMap<>();
		
		for (Long id : idsRecentes) {
		mapaDeMedias.put(id, service.getMediaDeAvaliacoes(id));
		mapaDeContagem.put(id, avaliacaoRepository.countByRestauranteId(id));
		}
		
		model.addAttribute("mapaDeMedias", mapaDeMedias);
		model.addAttribute("mapaDeContagem", mapaDeContagem);
		
		Long usuarioId = getCurrentUserId(); 
		Set<Long> idsFavoritos = (usuarioId != null) ? favoritoService.getFavoritoIds(usuarioId) : Collections.emptySet();
		model.addAttribute("idsFavoritos", idsFavoritos);
		
		return "inicial";
		}
    @GetMapping("/restaurantes")
    public String mostrarRestaurantes(
            @RequestParam(name = "culinaria", required = false) String culinaria,
            @RequestParam(name = "tipoDePrato", required = false) String tipoDePrato, 
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "9") int size,
            @RequestParam(name = "sortParam", defaultValue = "nome,ASC") String sortParam,
            Model model) {

        // 2. LÓGICA DE ORDENAÇÃO (para "quebrar" o sortParam)
        String[] sortParts = sortParam.split(","); // "nome,ASC" vira ["nome", "ASC"]
        String sortField = sortParts[0];
        String direction = sortParts[1];

        Sort sortOrder = Sort.by(Sort.Direction.fromString(direction), sortField);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<Restaurante> paginaRestaurantes; // Declara a página

        // 2. ÁRVORE DE DECISÃO PARA OS FILTROS
        boolean temCulinaria = (culinaria != null && !culinaria.isEmpty());
        boolean temTipo = (tipoDePrato != null && !tipoDePrato.isEmpty());

        if (temCulinaria && temTipo) {
            paginaRestaurantes = reposi.findByCulinariaAndTipodepratoContaining(culinaria, tipoDePrato, pageable);
        } else if (temCulinaria) {
            paginaRestaurantes = reposi.findByCulinaria(culinaria, pageable);
        } else if (temTipo) {
            paginaRestaurantes = reposi.findByTipodepratoContaining(tipoDePrato, pageable);
        } else {
            paginaRestaurantes = reposi.findAll(pageable); // Sem filtros
        }

        // --- 3. (Lógica de Favoritos, Média e Contagem - Sem alteração) ---
        Long usuarioId = getCurrentUserId(); 
        Set<Long> idsFavoritos = (usuarioId != null) ? favoritoService.getFavoritoIds(usuarioId) : Collections.emptySet();
        
        List<Long> idsDaPagina = paginaRestaurantes.getContent().stream()
                                    .map(Restaurante::getId)
                                    .collect(Collectors.toList());
        Map<Long, Double> mapaDeMedias = new HashMap<>();
        Map<Long, Long> mapaDeContagem = new HashMap<>();
        
        for (Long id : idsDaPagina) {
            mapaDeMedias.put(id, service.getMediaDeAvaliacoes(id));
            mapaDeContagem.put(id, avaliacaoRepository.countByRestauranteId(id));
        }

        // --- 4. ENVIAR OS FILTROS E OPÇÕES PARA O HTML ---
        
        // Pega as culinárias únicas (Verifique se seu 'reposi' tem esse método)
        model.addAttribute("culinariasUnicas", reposi.findDistinctCulinarias());
        
        // Lista fixa dos tipos de prato
        List<String> tiposDePratoUnicos = List.of("Rodízio", "Self Service", "A la Carte");
        model.addAttribute("tiposDePratoUnicos", tiposDePratoUnicos);
        
        // Envia os valores selecionados de volta
        model.addAttribute("culinariaSelecionada", culinaria);
        model.addAttribute("tipoDePratoSelecionado", tipoDePrato);
        model.addAttribute("sortParam", sortParam);

        // Envia os dados principais
        model.addAttribute("paginaRestaurantes", paginaRestaurantes); // <-- AQUI!
        model.addAttribute("idsFavoritos", idsFavoritos); 
        model.addAttribute("mapaDeMedias", mapaDeMedias);
        model.addAttribute("mapaDeContagem", mapaDeContagem);

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
            
            Double media = service.getMediaDeAvaliacoes(id); // Chama o novo método do service
            model.addAttribute("mediaAvaliacoes", media);

            List<Avaliacao> avaliacoes = avaliacaoRepository.findByRestauranteIdOrderByDataAvaliacaoDesc(id);
        	model.addAttribute("avaliacoesDoRestaurante", avaliacoes);
        	
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