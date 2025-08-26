package com.example.app1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/erro403")
    public String acessoNegado() {
        return "erro403"; // retorna o Thymeleaf
    }
}
