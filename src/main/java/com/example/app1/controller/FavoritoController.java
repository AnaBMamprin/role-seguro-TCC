package com.example.app1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.app1.model.Restaurante;
import com.example.app1.service.FavoritoService;

@Controller
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @GetMapping("/favoritos")
    public String listarFavoritos(Model model, Authentication authentication) {
        String emailUsuario = authentication.getName();
        List<Restaurante> favoritos = favoritoService.listarFavoritos(emailUsuario);
        model.addAttribute("favoritos", favoritos);
        return "favoritos";
    }
}
