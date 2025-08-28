package com.example.app1.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.app1.model.Usuario;
import com.example.app1.records.UserDTO;
import com.example.app1.repository.UserRepository;
import com.example.app1.usuarioEnums.UserEnum;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Cadastro
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
        user.setRole(userDTO.isAdmin() ? UserEnum.ADMIN : UserEnum.USER);

        userRepository.save(user);
        return true;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    // Edição de usuário existente
    @Transactional
    public boolean updateUser(UserDTO userDTO) {
        Usuario user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setNomeUsuario(userDTO.getNome());
        user.setEmailUsuario(userDTO.getEmail());
        user.setEnderecoUsuario(userDTO.getEndereco());

        if(userDTO.getSenha() != null && !userDTO.getSenha().isEmpty()) {
            user.setSenhaUsuario(passwordEncoder.encode(userDTO.getSenha()));
        }

        user.setRole(userDTO.isAdmin() ? UserEnum.ADMIN : UserEnum.USER);

        userRepository.save(user);
        return true;
    }
}