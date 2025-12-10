package com.example.app1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute; 
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping; 

import com.example.app1.model.Avaliacao;
import com.example.app1.model.Usuario;
import com.example.app1.records.AvaliacaoDTO;
import com.example.app1.repository.UserRepository;
import com.example.app1.service.AvaliacaoService;

@Controller
@RequestMapping("/restaurantes") 
public class AvaliacaoController {
	
	@Autowired
	AvaliacaoService serv;
	
	@Autowired
    private UserRepository userRepository;
	
	@GetMapping ("/avaliacoes")
	public String mostrarPaginaRestaurantes(Model model) {
		List<Avaliacao> listaDeAvaliacoes = serv.ListarTodasAvaliacoes();
		model.addAttribute("avaliacoes", listaDeAvaliacoes);
		model.addAttribute("novaAvaliacao", new AvaliacaoDTO());
		return "restaurantes"; 
	}

	@PostMapping("/salvar")
	public String salvarAvaliacao(@ModelAttribute AvaliacaoDTO ava) { 

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailLogado = null;

        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                emailLogado = ((UserDetails) principal).getUsername();
            } 
            else if (principal instanceof OAuth2User) {
                emailLogado = ((OAuth2User) principal).getAttribute("email");
            }
        }

        if (emailLogado == null) {
            return "redirect:/login";
        }

        final String emailFinal = emailLogado;

		Usuario usuarioLogado = userRepository.findByEmailUsuario(emailFinal)
	            .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o email: " + emailFinal));
        
		ava.setUsuarioId(usuarioLogado.getIdUsuario()); 
	        
		serv.SalvarAvaliacao(ava);
			
		return "redirect:/modelo-restaurante?id=" + ava.getRestauranteId();
	}
	
	@GetMapping("/deletar/avaliacao/{id}")
	public String deletarAvaliacao(@PathVariable("id") Long id) {
		serv.DeletarAvaliacao(id); 	 
		return "redirect:/restaurantes";
	}
}