package com.example.app1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app1.records.RestauranteDTO;
import com.example.app1.records.UserDTO;
import com.example.app1.service.UserService;



@RestController
public class UserController {
		
		
		
		 @Autowired
		    private UserService userservice;

		 @PostMapping("/cadastro")
		 public String salvarCadastro(@ModelAttribute UserDTO userRecordDTO) {
		     userservice.registerUser(userRecordDTO);
		     return "Usuario cadastrado com sucesso"; 
		 }
		
		 @PostMapping("/cadastrorestaurante")
		 public String salvarRestaurante(@ModelAttribute RestauranteDTO userRecordDTO) {
		     //mexer
		     return "Usuario cadastrado com sucesso"; 
		 }
		 
		    
		    
		
	 
}
