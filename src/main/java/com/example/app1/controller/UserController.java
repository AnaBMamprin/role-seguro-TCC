package com.example.app1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app1.records.RestauranteDTO;
import com.example.app1.records.UserDTO;
import com.example.app1.service.UserService;



@Controller
public class UserController {
		

		@Autowired
		private UserService userservice;

		 
		 @PostMapping("/cadastrar")
		 public String salvarCadastro(@ModelAttribute UserDTO userDTO, RedirectAttributes redirectAttributes) {
		     userservice.registerUser(userDTO);
		     redirectAttributes.addFlashAttribute("mensagem", "Cadastro realizado com sucesso!");
		     return "login"; 
		 }
		
		 @GetMapping("/cadastro")
		 public String mostrarFormulario(Model model) {
		     model.addAttribute("userDTO", new UserDTO()); // agora funciona
		     return "cadastro";
		 }
		 
		 
		 @PostMapping("/cadastrorestaurante")
		 public String salvarRestaurante(@ModelAttribute RestauranteDTO userRecordDTO) {
		     return "login"; 
		 }
		 
		    
		    
		
	 
}
