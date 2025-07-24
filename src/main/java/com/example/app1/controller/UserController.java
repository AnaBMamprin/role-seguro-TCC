package com.example.app1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.example.app1.model.User;
import com.example.app1.repository.UserRepository;

@RestController
public class UserController {
		
		
		
		 @Autowired
		    private UserRepository usrep;

		    @PostMapping("/cadastro")
		    public RedirectView cadastrarCliente(@RequestParam String nome,
		                                         @RequestParam String email,
		                                         @RequestParam String endereco,
		                                         @RequestParam String senha) {

		    	User user = new User(nome, email, senha, endereco);
		        usrep.save(user);

		        return new RedirectView("/login"); 
		    }
		
		
		
		@GetMapping ("api/{id}")
		public User findByid (@PathVariable Long id) {
			
			return usrep.getById(id);
		}
	
		@GetMapping	("api/listar")
		public List<User> listarUsuarios() {
			return usrep.findAll();
		}
		
	
}
