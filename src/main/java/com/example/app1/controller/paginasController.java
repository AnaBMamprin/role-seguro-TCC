package com.example.app1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.app1.model.Usuario;
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
	  
	//analisar este método @GetMapping("/cadastro")
	 public String mostrarFormulario(Usuario usuario) {
	     usuario.addAttribute("userRecordDTO", new UserRecordDTO(null, null, null, null));
	     return "cadastro"; // nome da view (ex: formulario.html)
	 }
	/*
	@GetMapping ("/home")
	public String home () {
		return "home";
	}*/

}
