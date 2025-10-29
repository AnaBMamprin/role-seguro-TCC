package com.example.app1.service;

import java.util.List;
import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app1.model.Avaliacao;
import com.example.app1.records.AvaliacaoDTO;
import com.example.app1.repository.AvaliacaoRepository;

@Service
public class AvaliacaoService {

	@Autowired
	private AvaliacaoRepository repo;
	
	
	public Avaliacao SalvarAvaliacao(AvaliacaoDTO avDTO) {
		 
		 Avaliacao novaAvaliacao = new Avaliacao();
		 
		 novaAvaliacao.setTituloAvaliacao(avDTO.getTituloAvaliação()); 
		 novaAvaliacao.setTextoAvaliacao(avDTO.getTextoAvaliação());
		 
		 return repo.save(novaAvaliacao);
	}
	
	public Avaliacao LerAvaliacao(Long id) {
		// repo.findById(id) retorna um Optional<Avaliacao>
		Optional<Avaliacao> optionalAva = repo.findById(id);
		
		return optionalAva.orElseThrow(() -> new RuntimeException("Avaliação não encontrada com o id: " + id));
	}

	public List<Avaliacao> ListarTodasAvaliacoes() {
		return repo.findAll();
	}
	
	public Avaliacao EditarAvaliacao (Long id, AvaliacaoDTO avDTO) { 
		Avaliacao avaliacaoExistente = LerAvaliacao(id); // Reutiliza o método de busca
		
		avaliacaoExistente.setTituloAvaliacao(avDTO.getTituloAvaliação());
		avaliacaoExistente.setTextoAvaliacao(avDTO.getTextoAvaliação());
		
		return repo.save(avaliacaoExistente);
	}


	public String DeletarAvaliacao (Long id) {
		if (!repo.existsById(id)) {
			throw new RuntimeException("Avaliação não encontrada com o id: " + id);
		}
		
		repo.deleteById(id);
		return "Avaliacao deletada com sucesso";
	}
	
}