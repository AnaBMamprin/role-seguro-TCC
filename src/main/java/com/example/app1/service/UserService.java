package com.example.app1.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.app1.model.Usuario;
import com.example.app1.records.UserDTO;
import com.example.app1.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
		@Autowired
	    private UserRepository userRepository;
		
		@Autowired
	    private PasswordEncoder passwordEncoder;

		@Transactional
		public boolean registerUser(UserDTO userDTO) {
		    if(userRepository.findByEmailUsuario(userDTO.getEmail()).isPresent()) {
		        return false; // já existe
		    }

		    Usuario user = new Usuario();
		    user.setNomeUsuario(userDTO.getNome());  
		    user.setEmailUsuario(userDTO.getEmail());
		    user.setEnderecoUsuario(userDTO.getEndereco());
		    user.setSenhaUsuario(passwordEncoder.encode(userDTO.getSenha()));
		    userRepository.save(user);
		    return true; // cadastrado com sucesso
		}
	

}
