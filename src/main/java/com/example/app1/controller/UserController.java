package com.example.app1.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.app1.records.UserRecordDTO;
import com.example.app1.service.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {
		
		
		
		 @Autowired
		    private UserService userservice;

		    @PostMapping("/cadastro")
		    public ResponseEntity<String> salvarCadastro( @Valid  @RequestBody  UserRecordDTO userRecordDTO ) {
		    	userservice.registerUser(userRecordDTO);
		    	return ResponseEntity.ok("Usuário Cadastrado com sucesso");
		    }
		
		
	 
}
