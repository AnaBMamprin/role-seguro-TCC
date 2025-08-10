package com.example.app1.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 
		
	    http
	    
	    	.csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(authz -> authz
	        		.requestMatchers("/", "/login", "/cadastro", "/home", "/css/**", "/js/**", "/images/**").permitAll()
	            .anyRequest().authenticated()
	        )
	        .formLogin(form -> form
	            .loginPage("/login") 
	            .defaultSuccessUrl("/home") 
	            .permitAll()
	        )
	        .logout(logout -> logout
	        	    .logoutUrl("/logout")  // 👈 Correto: URL para trigger do logout
	        	    .logoutSuccessUrl("/login?logout")  // Página após logout
	        	    .invalidateHttpSession(true)  // Encerra a sessão
	        	    .deleteCookies("JSESSIONID")  // Remove cookies
	        	    .permitAll()  // Permite acesso sem autenticação
	        	);
	    return http.build();
	}
       
	
	  	@Bean
	    public InMemoryUserDetailsManager userDetailsService() {
	        UserDetails user = User.withDefaultPasswordEncoder()
	            .username("luis")
	            .password(passwordEncoder().encode("toma"))
	            .roles("USER")
	            .build();
	        return new InMemoryUserDetailsManager(user);
	    }
	

    // Mantenha o PasswordEncoder (necessário para criptografia de senhas)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}