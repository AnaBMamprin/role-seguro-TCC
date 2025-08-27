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
    private RestauranteService restauranteService;

    // ================== RESTAURANTES ==================
    
    @GetMapping
    public String paginaAdm(Model model) {
        List<Restaurante> restaurantes = restauranteRepository.findAll();
        List<Usuario> usuarios = userRepository.findAll();
        model.addAttribute("restaurantes", restaurantes);
        model.addAttribute("usuarios", usuarios);
        return "adm";
    }

    @PostMapping("/restauranteCadastrar")
    public String cadastrarRestaurante(@ModelAttribute RestauranteDTO dto) {
        restauranteService.converteRestaurantes(dto);
        return "redirect:/adm";
    }

    @PostMapping("/restauranteEditar")
    public String editarRestaurante(@ModelAttribute RestauranteDTO dto, @RequestParam("id") Long id) {
        Restaurante restaurante = restauranteRepository.findById(id).orElse(null);
        if (restaurante != null) {
            restaurante.setNome(dto.getNome());
            restaurante.setCidade(dto.getCidade());
            restaurante.setCulinaria(dto.getCulinaria());
            restaurante.setTipodeprato(dto.getTipodeprato());
            restaurante.setHorario(dto.getHorario());
            restaurante.setEndereco(dto.getEndereco());
            restaurante.setSite(dto.getSite());
            restauranteRepository.save(restaurante);
        }
        return "redirect:/adm";
    }

    @PostMapping("/restauranteExcluir")
    public String excluirRestaurante(@RequestParam("id") Long id) {
        restauranteRepository.deleteById(id);
        return "redirect:/adm";
    }

    // ================== USUÁRIOS ==================

    @PostMapping("/usuarioToggleAdm")
    public String toggleAdm(@RequestParam("id") Long id) {
        Usuario usuario = userRepository.findById(id).orElse(null);
        if (usuario != null) {
            if (usuario.getRole() == UserEnum.USER) {
                usuario.setRole(UserEnum.ADMIN);
            } else {
                usuario.setRole(UserEnum.USER);
            }
            userRepository.save(usuario);
        }
        return "redirect:/adm";
    }

    @PostMapping("/usuarioExcluir")
    public String excluirUsuario(@RequestParam("id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/adm";
    }
}