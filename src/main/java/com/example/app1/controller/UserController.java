package com.example.app1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app1.records.UserDTO;
import com.example.app1.repository.UserRepository;
import com.example.app1.service.UserService;



@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Cadastro
    @PostMapping("/cadastrar")
    public String salvarCadastro(@ModelAttribute UserDTO userDTO, RedirectAttributes redirectAttributes) {
        userService.registerUser(userDTO);
        redirectAttributes.addFlashAttribute("mensagem", "Cadastro realizado com sucesso!");
        return "login";
    }

    @GetMapping("/cadastro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "cadastro";
    }

    // Edição de usuário
    @PostMapping("/adm/updateUser")
    public String updateUser(@ModelAttribute UserDTO userDTO, RedirectAttributes redirectAttributes) {
        userService.updateUser(userDTO);  // <-- chama o service aqui
        redirectAttributes.addFlashAttribute("mensagem", "Usuário atualizado com sucesso!");
        return "redirect:/adm";  // volta para a página de administração
    }
    
    // Método para deletar usuário
    @PostMapping("/adm/deleteUser")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id); // ✅ chama o service
        return "redirect:/adm";
    }
}
