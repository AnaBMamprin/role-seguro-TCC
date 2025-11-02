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
        user.setRole(userDTO.isAdmin() ? UserEnum.ROLE_ADMIN : UserEnum.ROLE_USER);

        userRepository.save(user);
        return true;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    public void updateUser(UserDTO userDTO) {
        
        // 1. Busca o usuário
        Usuario usuario = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 2. Atualiza os campos
        usuario.setNomeUsuario(userDTO.getNome());
        usuario.setEmailUsuario(userDTO.getEmail());
        // ... (lógica da senha que fizemos antes) ...
        if (userDTO.getSenha() != null && !userDTO.getSenha().isEmpty()) {
            usuario.setSenhaUsuario(passwordEncoder.encode(userDTO.getSenha()));
        }
        
        userRepository.save(usuario);
    }


    // --- [NOVO MÉTODO PARA O PERFIL] ---
    // (Ele usa o email da sessão e é chamado pelo /perfil/atualizar)
    // (Note que mudei o nome para 'atualizarPerfil' para ficar mais claro)
    @Transactional
    public void atualizarPerfil(String emailUsuarioLogado, Usuario dadosDoFormulario) {
        
        // 1. Busca o usuário *real* do banco pelo email da sessão
        Usuario usuarioParaAtualizar = userRepository.findByEmailUsuario(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o email: " + emailUsuarioLogado));

        // 2. Atualiza SÓ os campos seguros vindos do formulário
        //    (NUNCA atualize email, senha ou roles aqui sem verificação)
        usuarioParaAtualizar.setNomeUsuario(dadosDoFormulario.getNomeUsuario());
        
        // Se seu formulário de perfil permite mudar o endereço, adicione aqui
        // usuarioParaAtualizar.setEndereco(dadosDoFormulario.getEndereco()); 

        // 3. Salva o usuário atualizado
        userRepository.save(usuarioParaAtualizar);
    }
    
    
    
}