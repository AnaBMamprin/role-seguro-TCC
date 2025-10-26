package com.example.app1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.app1.model.Restaurante;
import com.example.app1.model.Usuario;
import com.example.app1.records.RestauranteDTO;
import com.example.app1.repository.RestauranteRepository;
import com.example.app1.repository.UserRepository;
import com.example.app1.service.RestauranteService;
import com.example.app1.usuarioEnums.UserEnum;

@Controller
@RequestMapping("/adm")
public class AdmController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestauranteService restauranteService; // <-- O Serviço

    // ================== RESTAURANTES ==================
    
    @GetMapping
    public String paginaAdm(Model model) {
        // ... (seu método está OK)
        List<Restaurante> restaurantes = restauranteRepository.findAll();
        List<Usuario> usuarios = userRepository.findAll();
        long totalAdmins = usuarios.stream()
               .filter(u -> u.getRole() == UserEnum.ROLE_ADMIN)
               .count();
        model.addAttribute("restaurantes", restaurantes);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalAdmins", totalAdmins);
        return "adm";
    }

    @PostMapping("/restauranteCadastrar")
    public String cadastrarRestaurante(@ModelAttribute RestauranteDTO dto) {
        // MÉTODO DE CADASTRO (ESTÁ CORRETO)
        restauranteService.converteRestaurantes(dto);
        return "redirect:/adm";
    }

    // ==============================================================
    // PARTE 2: MÉTODO DE EDIÇÃO CORRIGIDO
    // ==============================================================
    @PostMapping("/restauranteEditar")
    public String editarRestaurante(@ModelAttribute RestauranteDTO dto, @RequestParam("id") Long id) {
        
        // AGORA A LÓGICA DE EDIÇÃO (COM GEOCODING) ESTÁ NO SERVIÇO!
        restauranteService.atualizarRestaurante(id, dto);
        
        return "redirect:/adm";
    }
    // ==============================================================

    @PostMapping("/restauranteExcluir")
    public String excluirRestaurante(@RequestParam("id") Long id) {
        restauranteRepository.deleteById(id);
        return "redirect:/adm";
    }

    // ================== USUÁRIOS ==================

    @PostMapping("/usuarioToggleAdm")
    public String toggleAdm(@RequestParam("id") Long id) {
        // ... (seu método está OK)
        Usuario usuario = userRepository.findById(id).orElse(null);
        if (usuario != null) {
            if (usuario.getRole() == UserEnum.ROLE_USER) {
                usuario.setRole(UserEnum.ROLE_ADMIN);
            } else {
                usuario.setRole(UserEnum.ROLE_USER);
            }
            userRepository.save(usuario);
        }
        return "redirect:/adm";
    }
}