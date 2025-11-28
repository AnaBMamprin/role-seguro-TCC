package com.example.app1.config;
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

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		
	    http
	    
	    	.csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(authz -> authz
	        		.requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**", "/webjars/**").permitAll()
	                
	                .requestMatchers("/", "/inicial", "/restaurantes", "/modelo-restaurante", "/buscar").permitAll()
	                .requestMatchers("/login", "/cadastro", "/cadastrar", "/sobre").permitAll()
	                
	                .requestMatchers("/favoritos/add", "/favoritos/remove").permitAll()
	                
	                .requestMatchers("/adm/**").hasRole("ADMIN")
	                .requestMatchers("/favoritos", "/perfil/**").authenticated()
	        )
	        .formLogin(form -> form
	            .loginPage("/login")
	            .usernameParameter("emailUsuario")
	            .passwordParameter("senhaUsuario")
	            .defaultSuccessUrl("/inicial", true) 
	            .permitAll()
	        )
	        .logout(logout -> logout
	        	    .logoutUrl("/logout")
	        	    .logoutSuccessUrl("/login?logout")
	        	    .invalidateHttpSession(true)
	        	    .deleteCookies("JSESSIONID")
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