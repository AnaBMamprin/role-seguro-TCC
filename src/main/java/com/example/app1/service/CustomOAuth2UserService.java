package com.example.app1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.app1.model.Usuario;
import com.example.app1.repository.UserRepository;
import com.example.app1.usuarioEnums.UserEnum;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. Carrega o usuário do Google
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2. Extrai o email e nome
        String email = oAuth2User.getAttribute("email");
        String nome = oAuth2User.getAttribute("name");

        // 3. Verifica se já existe no banco; se não, salva
        processarUsuarioGoogle(email, nome);

        return oAuth2User;
    }

    private void processarUsuarioGoogle(String email, String nome) {
        Optional<Usuario> usuarioExistente = userRepository.findByEmailUsuario(email);

        if (usuarioExistente.isEmpty()) {
            // --- O TRUQUE PARA O SEU TCC ---
            // Como o banco exige senha e endereço, vamos criar dados "fakes"
            // para que o usuário consiga ser salvo sem erro.
            
            Usuario novoUsuario = new Usuario();
            novoUsuario.setEmailUsuario(email);
            novoUsuario.setNomeUsuario(nome);
            novoUsuario.setRole(UserEnum.ROLE_USER);
            
            // Campos obrigatórios que o Google não dá:
            novoUsuario.setEnderecoUsuario("Endereço pendente (Google Login)");
            novoUsuario.setSenhaUsuario("GOOGLE_AUTH_USER"); // Senha inútil, pois ele loga pelo Google

            userRepository.save(novoUsuario);
            System.out.println("Novo usuário do Google salvo: " + email);
        } else {
            // Se quiser atualizar o nome caso mude no Google:
            Usuario usuario = usuarioExistente.get();
            usuario.setNomeUsuario(nome);
            userRepository.save(usuario);
        }
    }
}
