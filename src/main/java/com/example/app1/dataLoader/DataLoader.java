package com.example.app1.dataLoader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.app1.model.Usuario;
import com.example.app1.repository.UserRepository;
import com.example.app1.usuarioEnums.UserEnum;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    // Construtor correto (sem void)
    public DataLoader(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.findByEmailLocal("admin@email.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNomeLocal("ADMIN");
            admin.setEmailLocal("admin@email.com");
            admin.setSenhaLocal(passwordEncoder.encode("senha123")); // Senha criptografada
            admin.setRole(UserEnum.ADMIN); // Define como administrador
            repository.save(admin);
        }
    }
}

