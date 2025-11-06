package com.example.app1.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	public String salvarAvaliacao( @ModelAttribute AvaliacaoDTO ava, Principal principal) { 
		Usuario usuarioLogado = userRepository.findByEmailUsuario(principal.getName())
	            .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + principal.getName()));
			ava.setUsuarioId(usuarioLogado.getIdUsuario()); 
	        // 8. SALVAR
			serv.SalvarAvaliacao(ava);
			
			return "redirect:/modelo-restaurante?id=" + ava.getRestauranteId();
		}
	
	@GetMapping("/deletar/avaliacao/{id}")
	public String deletarAvaliacao(@PathVariable("id") Long id) {
		serv.DeletarAvaliacao(id); 	 
		return "redirect:/restaurantes";
	}
}