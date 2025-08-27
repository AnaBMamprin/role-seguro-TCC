package com.example.app1.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
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
    public UserDetails loadUserByUsername(String emailUsuario) throws UsernameNotFoundException {
        
        Optional<Usuario> usuario = usuarioRepo.findByEmailUsuario(emailUsuario);
                
        
        return User.builder()
                .username(usuario.get().getUsername()) // Ou outro campo único
                .password(usuario.get().getPassword())
                .roles(usuario.get().getRole().name())
                .build();
        
        
        
    }

   }
