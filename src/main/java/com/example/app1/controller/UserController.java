package com.example.app1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app1.model.Usuario;
import com.example.app1.records.UserDTO;
import com.example.app1.repository.UserRepository;
import com.example.app1.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository usuarioRepository;

    @PostMapping("/cadastrar")
    public String salvarCadastro(@ModelAttribute UserDTO userDTO, RedirectAttributes redirectAttributes) {
        userService.registerUser(userDTO);
        redirectAttributes.addFlashAttribute("mensagem", "Cadastro realizado com sucesso!");
        return "login";
    }
    
    
    @GetMapping("/perfil")
    public String verPerfil(Model model) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailTemp = null;

        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                emailTemp = ((UserDetails) principal).getUsername();
            } 
            else if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
                emailTemp = ((org.springframework.security.oauth2.core.user.OAuth2User) principal).getAttribute("email");
            }
        }

        if (emailTemp == null) {
            return "redirect:/login";
        }

        final String emailFinal = emailTemp;

        Usuario usuario = usuarioRepository.findByEmailUsuario(emailFinal)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o email: " + emailFinal));

        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @PostMapping("/perfil/atualizar")
    public String atualizarPerfil(@ModelAttribute Usuario usuarioForm, RedirectAttributes redirectAttributes) {

        String emailLogado = getEmailUsuarioLogado();

        if (emailLogado == null) {
            return "redirect:/login";
        }

        try {
            userService.atualizarPerfil(emailLogado, usuarioForm);
            
            redirectAttributes.addFlashAttribute("sucesso", "Perfil atualizado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar perfil: " + e.getMessage());
        }

        return "redirect:/perfil";
    }

    private String getEmailUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } 
            else if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
                return ((org.springframework.security.oauth2.core.user.OAuth2User) principal).getAttribute("email");
            }
        }
        return null;
    }


    @PostMapping("/perfil/excluir")
    public String excluirPerfil(Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Usuario usuarioParaExcluir = usuarioRepository.findByEmailUsuario(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
            userService.deleteUser(usuarioParaExcluir.getIdUsuario());
            
            return "redirect:/login?logout";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Não foi possível excluir sua conta.");
            return "redirect:/perfil";
        }
    } 


    @GetMapping("/cadastro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "cadastro";
    }
    

  /*  // Edição de usuário
    @PostMapping("/perfil/atualizar")
    public String updateUser(@ModelAttribute UserDTO userDTO, RedirectAttributes redirectAttributes) {
        userService.updateUser(userDTO);
        redirectAttributes.addFlashAttribute("mensagem", "Usuário atualizado com sucesso!");
        return "redirect:/adm";
    } */
    
    // Método para deletar usuário
   /* @PostMapping("/adm/deleteUser")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id); // ✅ chama o service
        return "redirect:/adm";
    }*/
    
   /* @PostMapping("/adm/deleteUser")
    public String excluirPerfil(Authentication authentication, 
                                RedirectAttributes redirectAttributes, 
                                HttpServletRequest request) { // <--- 1. Injete o Request aqui
        try {
            String email = authentication.getName();
            Usuario usuarioParaExcluir = usuarioRepository.findByEmailUsuario(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
            // 2. Deleta o usuário do banco
            userService.deleteUser(usuarioParaExcluir.getIdUsuario());
            
            // 3. LIMPEZA MANUAL DA SESSÃO (O Pulo do Gato 🐈)
            SecurityContextHolder.clearContext(); // Remove a autenticação do Spring Security
            
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // Destroi o cookie de sessão (JSESSIONID)
            }
            
            return "redirect:/login?logout"; // Agora sim, redireciona limpo
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Não foi possível excluir sua conta.");
            return "redirect:/perfil";
        }
    }*/
    
    @PostMapping("/adm/deleteUser")
    public String deleteUser(@RequestParam("id") Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        
        String emailLogado = authentication.getName();
        Usuario adminLogado = usuarioRepository.findByEmailUsuario(emailLogado).get();

        if (adminLogado.getIdUsuario().equals(id)) {
            redirectAttributes.addFlashAttribute("erro", "Você não pode se excluir pelo painel administrativo!");
            return "redirect:/adm";
        }

        userService.deleteUser(id); 
        return "redirect:/adm";
    }
}
