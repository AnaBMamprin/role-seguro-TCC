package com.example.app1.controller;


import com.example.app1.model.Cupom;
import com.example.app1.model.Usuario;
import com.example.app1.repository.RestauranteRepository;
import com.example.app1.repository.UserRepository;
import com.example.app1.service.CupomService;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cupons")
public class CupomController {

    @Autowired
    private CupomService cupomService;
    
    @Autowired
    private UserRepository usuarioRepository; 
    
    @Autowired
    private RestauranteRepository restauranteRepository; 
    

    @PostMapping("/gerar")
    public String gerarCupom(@RequestParam Long usuarioId,
                             @RequestParam Long restauranteId,
                             Model model) {
        try {
            Cupom cupom = cupomService.gerarCupom(usuarioId, restauranteId);
            model.addAttribute("cupom", cupom);
            model.addAttribute("mensagem", "Cupom gerado com sucesso!");
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
        }

        // volta para a página adm.html
        return "adm";
    }
    
    @PostMapping("/cupons/gerar")
    public String gerarCupom(@RequestParam Long restauranteId,
                             Principal principal,
                             Model model) {
        try {
            // pega o usuário logado pelo e-mail
            Usuario usuario = usuarioRepository.findByEmailUsuario(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // passa o id do usuário logado + restaurante selecionado
            Cupom cupom = cupomService.gerarCupom(usuario.getIdUsuario(), restauranteId);
            model.addAttribute("cupom", cupom);

        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
        }

        // recarrega os restaurantes para a tela
        model.addAttribute("restaurantes", restauranteRepository.findAll());
        return "restaurantes";
    }
    
}

