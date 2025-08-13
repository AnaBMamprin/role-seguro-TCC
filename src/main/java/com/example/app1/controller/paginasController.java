package com.example.app1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app1.records.UserDTO;






@Controller
public class paginasController {
	
	@GetMapping ("/")
	public String home () {
		return "login";
	}
	
	 /*@GetMapping("/login")
	    public String showLoginPage(
	        @RequestParam(value = "error", required = false) String error,
	        @RequestParam(value = "logout", required = false) String logout,
	        Model model) {

	        if (error != null) {
	            model.addAttribute("error", "Credenciais inválidas!");
	        }
	        if (logout != null) {
	            model.addAttribute("message", "Logout realizado!");
	        }

	        return "login";
	    } */
	  
	 @GetMapping("/cadastro")
	 public String mostrarFormulario(Model model) {
	     model.addAttribute("userRecordDTO", new UserDTO()); // agora funciona
	     return "/cadastro";
	 }
	
	@GetMapping ("/home")
	public String mostrarhome(){
		return "home";
	}
	
/*	@GetMapping("/inicial")
	public String mostrarTelaInicial() {
	   return "Inicial";
	  } */
	  

}
