package com.example.app1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app1.model.Usuario;

public interface UserRepository extends JpaRepository <Usuario, Long> {

	Optional<Usuario> findByEmailLocal(String username);
	
}
