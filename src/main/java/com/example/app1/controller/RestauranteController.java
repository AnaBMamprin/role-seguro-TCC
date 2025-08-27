package com.example.app1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app1.model.Restaurante;
import com.example.app1.records.RestauranteDTO;
import com.example.app1.repository.RestauranteRepository;
import com.example.app1.service.RestauranteService;

@Controller
public class RestauranteController {
	
	
	@Autowired
    private RestauranteRepository reposi;
	@Autowired
    private RestauranteService service;

    public RestauranteController(RestauranteRepository repository) {
        this.reposi = repository;
    }

  /*  @GetMapping("/restaurantes")
    public String mostrarRestaurantes(@RequestParam("culinaria") String culinaria, Model model) {
        List<Restaurante> restaurantes = reposi.findByCulinaria(culinaria);
        model.addAttribute("restaurantes", restaurantes);
        model.addAttribute("culinaria", culinaria);
        return "restaurantes"; // nome do arquivo Thymeleaf: restaurantes.html
    }
    */
    
    @GetMapping("/Listarrestaurantes")
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

        return "restaurantes"; 
    }
   
    
    @PostMapping("/restauranteCadastrar")
    public String cadastroRestaurante(@ModelAttribute RestauranteDTO restauranteDTO) {
    	service.converteRestaurantes(restauranteDTO);
    	return "redirect:/adm";		
       	}
    
}


