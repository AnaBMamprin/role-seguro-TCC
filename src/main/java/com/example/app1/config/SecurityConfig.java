package com.example.app1.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.csrf(Customizer.withDefaults()) 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/cadastro", "/login").permitAll() // Público
                .requestMatchers("/admin/**").hasRole("USER") // Exige ROLE_USER
                .anyRequest().authenticated() // Exige login para outras rotas
            )
            .formLogin(form -> form
                .loginPage("/login") // Página de login personalizada (se tiver)
                .defaultSuccessUrl("/Inicial") // Rota após login bem-sucedido
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Para criptografar senhas
    }
}