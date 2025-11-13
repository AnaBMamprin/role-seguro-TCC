package com.example.app1.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app1.model.Usuario;
import com.example.app1.usuarioEnums.UserEnum;

public interface UserRepository extends JpaRepository <Usuario, Long> {

	 Optional<Usuario> findByEmailUsuario(String email);
	 
	 long countByRole(UserEnum role);
	 
	
}
