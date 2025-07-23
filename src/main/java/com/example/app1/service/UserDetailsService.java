package com.example.app1.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.app1.model.User;
import com.example.app1.repository.UserRepository;

@Service
public class UserDetailsService {

    @Autowired
    private UserRepository usuarioRepo;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User usuario = usuarioRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return new org.springframework.security.core.userdetails.User(
            usuario.getEmail(),
            usuario.getSenha(),
            Collections.singletonList(new SimpleGrantedAuthority("USER"))
        );
    }
}