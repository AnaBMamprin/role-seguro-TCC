package com.example.app1.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app1.model.User;
import com.example.app1.repository.UserRepository;

@RestController
public class UserController {
		
		UserRepository rep;
		
		@PostMapping ("api/salvar")
		public void SalvarUsuario(User usuario) {
			 rep.save(usuario);
		}
		
		@GetMapping ("api/{id}")
		public User findByid (@PathVariable Long id) {
			
			return rep.getById(id);
		}
	
		@GetMapping	("api/listar")
		public List<User> listarUsuarios() {
			return rep.findAll();
		}
		
	
}
