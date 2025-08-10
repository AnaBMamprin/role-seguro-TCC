package com.example.app1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.app1.records.UserRecordDTO;

import ch.qos.logback.core.model.Model;





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
	  
	 @GetMapping("/cadastro")
	 public String mostrarPaginaCadastro(Model model) {
	     model.addAttribute("userRecordDTO", new UserRecordDTO("", "", "", "")); // Objeto vazio
	     return "cadastro";
	 }
	 
	/*
	@GetMapping ("/home")
	public String home () {
		return "home";
	}*/

}
