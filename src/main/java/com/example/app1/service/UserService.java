package com.example.app1.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.app1.model.Usuario;
import com.example.app1.records.UserRecordDTO;
import com.example.app1.repository.UserRepository;

@Service
public class UserService {
		@Autowired
	    private final UserRepository userRepository;
		
		@Autowired
	    private final PasswordEncoder passwordEncoder;

	    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
	        this.userRepository = userRepository;
	        this.passwordEncoder = passwordEncoder;
	    }

	    public void registerUser(UserRecordDTO userDTO) {
	        Usuario user = new Usuario();
	        user.setNomeLocal(userDTO.nomeLocal());
	        user.setEmailLocal(userDTO.emailLocal());
	        user.setEnderecoLocal(userDTO.enderecoLocal());
	        user.setSenhaLocal(passwordEncoder.encode(userDTO.senhaLocal())); // Criptografa a senha!
	        user.setRole("ROLE_USER");

	        userRepository.save(user);
	    }
	
	

}
