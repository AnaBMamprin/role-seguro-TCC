package com.example.app1.controller;

import com.example.app1.model.Cupom;
import com.example.app1.model.Restaurante; // <-- IMPORT NECESSÁRIO
import com.example.app1.model.Usuario;
import com.example.app1.repository.RestauranteRepository;
import com.example.app1.repository.UserRepository;
import com.example.app1.service.CupomService;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // <-- IMPORT NECESSÁRIO

@Controller
@RequestMapping("/cupons")
public class CupomController {

    @Autowired
    private CupomService cupomService;
    
    @Autowired
    private UserRepository usuarioRepository; 
    
    @Autowired
    private RestauranteRepository restauranteRepository; 

    // ==========================================================
    // === FLUXO 1: CLIENTE PEGA UM CUPOM
    // ==========================================================

    /**
     * Mostra a página para o CLIENTE escolher um restaurante para gerar cupom.
     */
    @GetMapping("/gerar")
    public String mostrarPaginaGerarCupomCliente(Model model, 
                                               @RequestParam(defaultValue = "0") int page) {
        
        int pageSize = 10; // Defina quantos restaurantes por página
        Pageable pageable = PageRequest.of(page, pageSize);
        
        // CORREÇÃO: Chame o findAll com pageable
        Page<Restaurante> paginaDeRestaurantes = restauranteRepository.findAll(pageable);
        
        // Envie o objeto Page inteiro para o Thymeleaf
        model.addAttribute("restaurantes", paginaDeRestaurantes);
        
        return "restaurantes"; // Página onde o cliente vê os restaurantes
    }

 
    @PostMapping("/gerar")
    public String gerarCupomCliente(@RequestParam Long restauranteId,
                                    Principal principal,
                                    Model model) {
        try {
            // 1. Acha o CLIENTE logado
            Usuario usuario = usuarioRepository.findByEmailUsuario(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado. Faça login."));
            
            // 2. Chama o service do FLUXO 1
            Cupom cupom = cupomService.gerarCupom(usuario.getIdUsuario(), restauranteId); 
            
            model.addAttribute("sucesso", "Cupom " + cupom.getCodigo() + " gerado com sucesso!");

        } catch (RuntimeException e) {
            // Se o service jogar um erro (ex: "cupom já resgatado"), ele cai aqui
            model.addAttribute("erro", e.getMessage());
        }
        
        // Recarrega os restaurantes e mostra a mesma página com a mensagem
        model.addAttribute("restaurantes", restauranteRepository.findAll());
        return "restaurantes";
    }

    
    // ==========================================================
    // === FLUXO 2: DONO DO RESTAURANTE CRIA UM CUPOM
    // ==========================================================

    /**
     * Mostra o formulário para o DONO DO RESTAURANTE preencher
     * (email do cliente, percentual, etc.)
    **/
    @GetMapping("/restaurante/gerar-formulario")
    public String mostrarFormularioGerarCupomDono(Model model) {
        // Retorna o caminho do seu novo arquivo HTML
        return "restaurante/painel-cupons"; 
    }
    
    /**
     * Processa o formulário enviado pelo DONO DO RESTAURANTE.
     */
  /*  @PostMapping("/restaurante/gerar")
    public String gerarCupomDirecionado(
            @RequestParam("emailCliente") String emailCliente,
            @RequestParam("percentualDesconto") Integer percentualDesconto,
            @RequestParam(value = "tipoCupom", defaultValue = "DIRECIONADO") String tipoCupom,
            Principal principal, // O Dono logado
            RedirectAttributes redirectAttributes) {

        try {
            // 1. Achar o Dono (que é um Usuario)
            Usuario donoLogado = usuarioRepository.findByEmailUsuario(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Dono não autenticado."));

            // 2. Achar o Restaurante que esse Dono gerencia
            // (Exige o método findByUsuario no RestauranteRepository)
            Restaurante restauranteDoDono = restauranteRepository.findByUsuario(donoLogado)
                    .orElseThrow(() -> new RuntimeException("Restaurante não encontrado para este dono."));
            
            // 3. Chama o service do FLUXO 2
            cupomService.gerarCupomDirecionado(
                    emailCliente,
                    percentualDesconto,
                    restauranteDoDono, // Passa o objeto Restaurante
                    tipoCupom
            );

            redirectAttributes.addFlashAttribute("sucesso", "Cupom gerado com sucesso para " + emailCliente + "!");

        } catch (RuntimeException e) {
            // Pega erros do service (ex: "email do cliente não existe")
            redirectAttributes.addFlashAttribute("erro", "Erro ao gerar cupom: " + e.getMessage());
        }

        // Usa redirect para evitar reenvio do formulário
        return "redirect:/cupons/restaurante/gerar-formulario";
    }
    */
    
    
}