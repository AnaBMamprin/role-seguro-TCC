package com.example.app1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app1.model.User;

public interface UserRepository extends JpaRepository <User, Long> {

	Object findByEmail(String username);
	
}
