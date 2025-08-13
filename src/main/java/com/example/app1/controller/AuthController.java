package com.example.app1.controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // página HTML do login
    }

    @GetMapping("/inicial")
    public String inicialPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // pega o "username" (no seu caso email)
        model.addAttribute("email", email);
        return "Inicial"; // página inicial após login
    }

    @GetMapping("/logout-success")
    public String logoutPage() {
        return "logout"; // página de confirmação de logout
    }

}
