package com.example.app1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.app1.model.Usuario;
import com.example.app1.repository.UserRepository;

@Service
public class MeuUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository usuarioRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        Usuario usuario = usuarioRepo.findByEmailLocal(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        		return usuario;
    }
   }
