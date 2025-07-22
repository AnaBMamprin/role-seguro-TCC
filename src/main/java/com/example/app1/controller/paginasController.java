package com.example.app1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class paginasController {
	
	@GetMapping ("/")
	public String home () {
		return "home";
	}
	
	 @GetMapping("/login")
    public String mostrarTelaLogin() {
        return "login"; // vai procurar login.html em /templates
    }
	  
	@GetMapping ("/cadastro")
	public String cadastro () {
		return "cadastro";
	}
	/*
	@GetMapping ("/home")
	public String home () {
		return "home";
	}*/

}
