package com.example.app1.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class paginasController {
	
	@GetMapping ("/")
	public String home () {
		return "login";
	}
	
/*	 @GetMapping("/cadastro")
	 public String mostrarFormulario(Model model) {
	     model.addAttribute("userRecordDTO", new UserDTO()); // agora funciona
	     return "cadastro";
	 }
*/
	@GetMapping ("/perfil")
	public String mostrarPerfil() {
		return "perfil";
	}
	
	  @GetMapping ("/restaurantes")
		public String mostrarRestaurantes() {
			return "restaurantes";
		}

}
