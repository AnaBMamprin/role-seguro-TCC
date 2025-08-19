package com.example.app1.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app1.model.Restaurante;
import com.example.app1.repository.RestauranteRepository;

@Controller
public class RestauranteController {

    private RestauranteRepository reposi;

    public RestauranteController(RestauranteRepository repository) {
        this.reposi = repository;
    }

    @GetMapping("/restaurantes")
    public String mostrarRestaurantes(@RequestParam("culinaria") String culinaria, Model model) {
        List<Restaurante> restaurantes = reposi.findByCulinaria(culinaria);
        model.addAttribute("restaurantes", restaurantes);
        model.addAttribute("culinaria", culinaria);
        return "restaurantes"; // nome do arquivo Thymeleaf: restaurantes.html
    }
}


