package com.example.app1.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.app1.model.Usuario;
import com.example.app1.records.UserRecordDTO;
import com.example.app1.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
		@Autowired
	    private UserRepository userRepository;
		
		@Autowired
	    private PasswordEncoder passwordEncoder;

		@Transactional
	    public void registerUser(UserRecordDTO userDTO) {
	        Usuario user = new Usuario();
	        user.setNomeLocal(userDTO.nomeLocal());
	        user.setEmailLocal(userDTO.emailLocal());
	        user.setEnderecoLocal(userDTO.enderecoLocal());
	        user.setSenhaLocal(passwordEncoder.encode(userDTO.senhaLocal()));
	        userRepository.save(user);
	    }
	
	

}
