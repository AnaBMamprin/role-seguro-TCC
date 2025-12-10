package com.example.app1.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
    private com.example.app1.service.CustomOAuth2UserService customOAuth2UserService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(authz -> authz
	            // Arquivos estáticos
	            .requestMatchers("/css/**", "/js/**", "/imagens/**", "/uploads/**", "/webjars/**").permitAll()
	            
	            // Páginas públicas
	            .requestMatchers("/", "/inicial", "/restaurantes", "/modelo-restaurante", "/buscar").permitAll()
	            .requestMatchers("/login", "/cadastro", "/cadastrar", "/sobre").permitAll()
	            
	            // PÁGINAS DE ERRO (O segredo está aqui)
	            .requestMatchers("/erro404").permitAll()
	            .requestMatchers("/erro-usuario-duplicado").permitAll()
	            .requestMatchers("/error").permitAll()
	            .requestMatchers("/erro403").permitAll() // <--- ADICIONE ESTA LINHA OBRIGATORIAMENTE!
	            
	            // Funcionalidades
	            .requestMatchers("/favoritos/add", "/favoritos/remove").permitAll()
	            
	            // Restrições
	            .requestMatchers("/adm/**").hasRole("ADMIN")
	            .requestMatchers("/favoritos", "/perfil/**").authenticated()
	            
	            // Qualquer outra coisa precisa de login
	            .anyRequest().authenticated()
	        )
	        .formLogin(form -> form
	            .loginPage("/login")
	            .usernameParameter("emailUsuario")
	            .passwordParameter("senhaUsuario")
	            .defaultSuccessUrl("/inicial", true) 
	            .permitAll()
	        )
	        
	        .oauth2Login(oauth2 -> oauth2
	                .loginPage("/login") // Usa a mesma página de login
	                .userInfoEndpoint(userInfo -> userInfo
	                    .userService(customOAuth2UserService) // Usa nosso serviço que salva no banco
	                )
	                .defaultSuccessUrl("/inicial", true) // Para onde vai depois de logar
	            )
	        
	        .logout(logout -> logout

	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/login?logout")
	            .invalidateHttpSession(true)
	            .deleteCookies("JSESSIONID")
	            .permitAll() 
	        )
	        .exceptionHandling(ex -> ex
	            .accessDeniedPage("/erro403") 
	        );

	    return http.build();
	}
       
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}