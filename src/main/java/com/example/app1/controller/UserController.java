package com.example.app1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app1.model.Usuario;
import com.example.app1.records.UserDTO;
import com.example.app1.repository.UserRepository;

@Controller
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/cadastrar")
    public String cadastrarUsuario(@ModelAttribute UserDTO userDTO, Model model) {
        if (userDTO.getSenha() == null || userDTO.getSenha().isEmpty()) {
            model.addAttribute("mensagem", "Senha não pode ser vazia!");
            return "usuarioExistente";
        }

        Usuario usuario = new Usuario();
        usuario.setNomeLocal(userDTO.getNome());
        usuario.setEmailLocal(userDTO.getEmail());
        usuario.setEnderecoLocal(userDTO.getEndereco());
        usuario.setSenhaLocal(passwordEncoder.encode(userDTO.getSenha()));

        repository.save(usuario);
        return "redirect:/login";
    }

}
