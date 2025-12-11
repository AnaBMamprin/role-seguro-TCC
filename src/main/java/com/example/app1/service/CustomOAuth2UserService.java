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
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String nome = oAuth2User.getAttribute("name");

        processarUsuarioGoogle(email, nome);

        return oAuth2User;
    }

    private void processarUsuarioGoogle(String email, String nome) {
        Optional<Usuario> usuarioExistente = userRepository.findByEmailUsuario(email);

        if (usuarioExistente.isEmpty()) {
        	
            Usuario novoUsuario = new Usuario();
            novoUsuario.setEmailUsuario(email);
            novoUsuario.setNomeUsuario(nome);
            novoUsuario.setRole(UserEnum.ROLE_USER);

            novoUsuario.setEnderecoUsuario("Endereço pendente (Google Login)");
            novoUsuario.setSenhaUsuario("GOOGLE_AUTH_USER");

            userRepository.save(novoUsuario);
            System.out.println("Novo usuário do Google salvo: " + email);
        } else {
            Usuario usuario = usuarioExistente.get();
            usuario.setNomeUsuario(nome);
            userRepository.save(usuario);
        }
    }
}
