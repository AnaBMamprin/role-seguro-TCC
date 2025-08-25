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
	        		.requestMatchers("/", "/login", "/cadastro", "cadastrar", "/css/**", "/js/**", "/images/**").permitAll()
	            .anyRequest().authenticated()
	        )
	        .formLogin(form -> form
	            .loginPage("/login")
	            .usernameParameter("emailLocal")
	            .passwordParameter("SenhaLocal")
	            .defaultSuccessUrl("/inicial", true) 
	            .permitAll()
	        )
	        .logout(logout -> logout
	        	    .logoutUrl("/logout")  // Correto: URL para trigger do logout
	        	    .logoutSuccessUrl("/login?logout")  // Página após logout
	        	    .invalidateHttpSession(true)  // Encerra a sessão
	        	    .deleteCookies("JSESSIONID")  // Remove cookies
	        	    .permitAll()  // Permite acesso sem autenticação
	        	);
	    return http.build();
	}
       
	
	 

    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}