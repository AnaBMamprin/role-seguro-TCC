package com.example.app1.controller;

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
import com.example.app1.records.AvaliacaoDTO;
import com.example.app1.service.AvaliacaoService;

@Controller
@RequestMapping("/restaurantes") 
public class AvaliacaoController {
	
	@Autowired
	AvaliacaoService serv;
	
	@GetMapping ("/avaliacoes")
	public String mostrarPaginaRestaurantes(Model model) {
		List<Avaliacao> listaDeAvaliacoes = serv.ListarTodasAvaliacoes();
		model.addAttribute("avaliacoes", listaDeAvaliacoes);
		model.addAttribute("novaAvaliacao", new AvaliacaoDTO("", ""));
		return "restaurantes"; 
	}
	
	@PostMapping("/salvar")
	public String salvarAvaliacao(AvaliacaoDTO ava) { 
		
		serv.SalvarAvaliacao(ava);
		return "redirect:/restaurantes";
	}
	
	@GetMapping("/deletar/avaliacao/{id}")
	public String deletarAvaliacao(@PathVariable("id") Long id) {
		serv.DeletarAvaliacao(id); 	 
		return "redirect:/restaurantes";
	}
}