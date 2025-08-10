package com.example.app1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app1.records.UserDTO;
import com.example.app1.service.UserService;

import jakarta.validation.Valid;



@RestController
public class UserController {
		
		
		
		 @Autowired
		    private UserService userservice;

		 @PostMapping("/cadastro")
		 public String salvarCadastro(@ModelAttribute UserDTO userRecordDTO) {
		     userservice.registerUser(userRecordDTO);
		     return "Usuario cadastrado com sucesso"; 
		 }
		
		    
		    
		
	 
}
