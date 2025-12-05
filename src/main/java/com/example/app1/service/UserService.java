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

    @Transactional
    public boolean registerUser(UserDTO userDTO) {
        if(userRepository.findByEmailUsuario(userDTO.getEmail()).isPresent()) {
            return false;
        }

        Usuario user = new Usuario();
        user.setNomeUsuario(userDTO.getNome());
        user.setEmailUsuario(userDTO.getEmail());
        user.setEnderecoUsuario(userDTO.getEndereco());
        user.setSenhaUsuario(passwordEncoder.encode(userDTO.getSenha()));
        user.setRole(userDTO.isAdmin() ? UserEnum.ROLE_ADMIN : UserEnum.ROLE_USER);

        userRepository.save(user);
        return true;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    public void updateUser(UserDTO userDTO) {

        Usuario usuario = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setNomeUsuario(userDTO.getNome());
        usuario.setEmailUsuario(userDTO.getEmail());
        if (userDTO.getSenha() != null && !userDTO.getSenha().isEmpty()) {
            usuario.setSenhaUsuario(passwordEncoder.encode(userDTO.getSenha()));
        }
        
        userRepository.save(usuario);
    }

    @Transactional
    public void atualizarPerfil(String emailUsuarioLogado, Usuario dadosDoFormulario) {

        Usuario usuarioParaAtualizar = userRepository.findByEmailUsuario(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o email: " + emailUsuarioLogado));

        // 1. Atualiza o Nome
        usuarioParaAtualizar.setNomeUsuario(dadosDoFormulario.getNomeUsuario());
        
        // 2. Atualiza o Endereço (CORRIGIDO)
        // (Verifique se o método é getEndereco() ou getEnderecoUsuario() na sua classe Usuario)
        usuarioParaAtualizar.setEnderecoUsuario(dadosDoFormulario.getEnderecoUsuario()); 

        // 3. Salva
        userRepository.save(usuarioParaAtualizar);
    }
    
    
    
}