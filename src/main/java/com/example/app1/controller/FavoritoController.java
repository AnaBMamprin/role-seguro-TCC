package com.example.app1.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import com.example.app1.repository.UserRepository;
import com.example.app1.service.FavoritoService;

@Controller
public class FavoritoController {

	private final FavoritoService favoritoService;
    private final UserRepository userRepository;
    
    @Autowired
    public FavoritoController(FavoritoService favoritoService, 
                              UserRepository userRepository) {
        this.favoritoService = favoritoService;
        this.userRepository = userRepository;
    }

    @GetMapping("/favoritos")
    public String listarFavoritos(Model model) { // Removido Authentication daqui
        Long usuarioId = getCurrentUserId(); // Pega o ID do usuário logado
        if (usuarioId == null) {
            return "redirect:/login"; // Redireciona se não estiver logado
        }

        List<Restaurante> favoritos = favoritoService.listarFavoritosPorUsuarioId(usuarioId);
        Set<Long> idsFavoritos = favoritoService.getFavoritoIds(usuarioId); // Pega os IDs

        model.addAttribute("favoritos", favoritos);
        model.addAttribute("idsFavoritos", idsFavoritos); // Envia os IDs para o Thymeleaf

        return "favoritos";
    }

    // ======================================================
    // ENDPOINT PARA ADICIONAR (POST AJAX)
    // ======================================================
    @PostMapping("/favoritos/add")
    @ResponseBody // Retorna dados, não HTML
    public ResponseEntity<?> adicionarFavorito(@RequestParam("restauranteId") Long restauranteId) {
        Long usuarioId = getCurrentUserId();
        if (usuarioId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado.");
        }

        boolean success = favoritoService.addFavorito(usuarioId, restauranteId);
        if (success) {
            return ResponseEntity.ok().body("Favorito adicionado."); // Mensagem de sucesso
        } else {
            // Pode ser erro de ID não encontrado ou outra falha
            return ResponseEntity.badRequest().body("Erro ao adicionar favorito.");
        }
    }

    // ======================================================
    // ENDPOINT PARA REMOVER (POST AJAX)
    // ======================================================
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


    // ======================================================
    // MÉTODO AUXILIAR PARA PEGAR O ID DO USUÁRIO LOGADO
    // ======================================================
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            String email;
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                email = ((UserDetails) principal).getUsername();
            } else if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
                 // Para login com Google, o email pode estar em um atributo diferente
                 email = ((org.springframework.security.oauth2.core.user.OAuth2User) principal).getAttribute("email");
            }
             else {
                 return null; // Não conseguiu determinar o usuário
            }

            if (email != null) {
                // Use o método correto do seu UserRepository
                Optional<Usuario> userOpt = userRepository.findByEmailUsuario(email); 
                return userOpt.map(Usuario::getIdUsuario).orElse(null); // Ajuste getIdUsuario se necessário
            }
        }
        return null; // Usuário não autenticado ou anônimo
    }
}
