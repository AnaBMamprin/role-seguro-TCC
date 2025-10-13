package com.example.app1.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app1.model.Restaurante;
import com.example.app1.repository.RestauranteRepository;
import com.example.app1.service.RestauranteService;

@Controller
public class RestauranteController {

    @Autowired
    private final RestauranteRepository reposi;

    @Autowired
    private final RestauranteService service;

    public RestauranteController(RestauranteRepository repository, RestauranteService service) {
    	this.reposi = repository;
        this.service = service;
    }

    // =================== LISTA PÚBLICA ===================
    // Lista restaurantes para os usuários (com filtro opcional por culinária)
    @GetMapping("/restaurantes")
    public String mostrarRestaurantes(
            @RequestParam(name = "culinaria", required = false) String culinaria,
            Model model) {

        List<Restaurante> restaurantes;

        if (culinaria != null && !culinaria.isEmpty()) {
            restaurantes = reposi.findByCulinaria(culinaria);
        } else {
            restaurantes = reposi.findAll();
        }

        model.addAttribute("restaurantes", restaurantes);
        model.addAttribute("culinaria", culinaria);

        return "restaurantes"; // renderiza restaurantes.html
    }
    
    @GetMapping("/modelo-restaurante")
    public String detalhesRestaurante(@RequestParam("id") Long id, Model model) {
        
        Optional<Restaurante> restauranteOpt = reposi.findById(id);

        if (restauranteOpt.isPresent()) {
            model.addAttribute("restaurante", restauranteOpt.get());
            return "modelo-restaurante"; 
        } else {
            return "redirect:/restaurantes";
        }
    }
}