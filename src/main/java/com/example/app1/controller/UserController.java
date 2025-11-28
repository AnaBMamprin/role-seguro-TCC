package com.example.app1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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
    // Cadastro
    @PostMapping("/cadastrar")
    public String salvarCadastro(@ModelAttribute UserDTO userDTO, RedirectAttributes redirectAttributes) {
        userService.registerUser(userDTO);
        redirectAttributes.addFlashAttribute("mensagem", "Cadastro realizado com sucesso!");
        return "login";
    }
    
    
    @GetMapping("/perfil")
    public String verPerfil(Model model, Authentication authentication) {
        
        // 1. Pega o email (ou username) do usuário logado na sessão
        String email = authentication.getName(); 
        
        // 2. Busca o usuário completo no banco de dados
        // (Você pode precisar criar este método no seu Repository)
        Usuario usuario = usuarioRepository.findByEmailUsuario(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        // 3. Adiciona o objeto "usuario" ao Model
        // A página Thymeleaf terá acesso a essa variável
        model.addAttribute("usuario", usuario); 
        
        return "perfil"; // Renderiza o arquivo 'perfil.html'
    }

    // (Vamos precisar disso para o botão "Salvar")
    @PostMapping("/perfil/atualizar")
    public String atualizarPerfil(
            @ModelAttribute("usuario") Usuario usuarioForm, // 1. Recebe o objeto do formulário
            Authentication authentication,                 // 2. Pega o usuário logado
            RedirectAttributes redirectAttributes) {
        
        try {
            String emailDoUsuarioLogado = authentication.getName();
            
            // 3. Passa para o service o email de quem está logado
            //    E os dados do formulário (nome, endereco, etc.)
            //    Você precisará criar ou ajustar este método no seu service!
            userService.atualizarPerfil(emailDoUsuarioLogado, usuarioForm);
            
            redirectAttributes.addFlashAttribute("sucesso", "Perfil atualizado!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Não foi possível atualizar o perfil.");
        }
        
        return "redirect:/perfil";
    }

    // (Vamos precisar disso para o botão "Excluir")
    @PostMapping("/perfil/excluir")
    public String excluirPerfil(Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Usuario usuarioParaExcluir = usuarioRepository.findByEmailUsuario(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
            userService.deleteUser(usuarioParaExcluir.getIdUsuario());
            
            return "redirect:/login?logout"; // Desloga
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
        
        // 1. Descobre quem é o Admin logado
        String emailLogado = authentication.getName();
        Usuario adminLogado = usuarioRepository.findByEmailUsuario(emailLogado).get();

        // 2. Proteção: Se o Admin tentar se deletar por aqui, bloqueia
        if (adminLogado.getIdUsuario().equals(id)) {
            redirectAttributes.addFlashAttribute("erro", "Você não pode se excluir pelo painel administrativo!");
            return "redirect:/adm";
        }

        // 3. Se for outro usuário, deleta normal
        userService.deleteUser(id); 
        return "redirect:/adm";
    }
}
