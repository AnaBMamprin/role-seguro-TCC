package com.example.app1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class Configuracoes {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        // Cria usuário com senha codificada
        UserDetails user = User.builder()
                .username("meuusuario") // seu usuário
                .password(encoder.encode("minhasenha")) // sua senha (codificada!)
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/cadastro").permitAll() // libera acesso ao cadastro
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") // sua página personalizada
                .permitAll()         // permite acesso a ela sem login
            )
            .logout();

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Encoder seguro de senhas
    }
}
