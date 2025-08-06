package com.example.app1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.app1.model.Usuario;
import com.example.app1.records.UserRecordDTO;



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
	  
	/* @GetMapping("/cadastro")
	 public String mostrarFormulario(Model model) {
	     model.addAttribute("userRecordDTO", new UserRecordDTO(null, null, null, null));
	     return "cadastro"; 
	 }
	/*
	@GetMapping ("/home")
	public String home () {
		return "home";
	}*/

}
