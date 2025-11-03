package com.example.app1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app1.model.Avaliacao;
import com.example.app1.model.Restaurante;
import com.example.app1.model.Usuario;
import com.example.app1.records.AvaliacaoDTO;
import com.example.app1.repository.AvaliacaoRepository;
import com.example.app1.repository.RestauranteRepository;
import com.example.app1.repository.UserRepository;

@Service
public class AvaliacaoService {

	@Autowired
	private AvaliacaoRepository repo;
	
	@Autowired
	private UserRepository usuarioRepository;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	public Avaliacao SalvarAvaliacao(AvaliacaoDTO avDTO) {
		 
        // 1. Buscar as entidades "pai" (Quem escreveu e Para qual restaurante)
        Usuario usuario = usuarioRepository.findById(avDTO.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + avDTO.getUsuarioId()));
        
        Restaurante restaurante = restauranteRepository.findById(avDTO.getId())
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado com ID: " + avDTO.getRestauranteId()));
		
        // 2. Criar a nova avaliação
		 Avaliacao novaAvaliacao = new Avaliacao();
		 
        // 3. Preencher os dados
		 novaAvaliacao.setTituloAvaliacao(avDTO.getTituloAvaliação()); 
		 novaAvaliacao.setTextoAvaliacao(avDTO.getTextoAvaliação());
        //novaAvaliacao.setNota(avDTO.nota()); // <-- Salvar a nota
		 
        // 4. --- A CORREÇÃO CRÍTICA ---
        // Ligar a avaliação ao usuário e ao restaurante
        novaAvaliacao.setUsuario(usuario);
        novaAvaliacao.setRestaurante(restaurante);
        // -----------------------------
        
		 return repo.save(novaAvaliacao);
	}
	
	public Avaliacao LerAvaliacao(Long id) {
		Optional<Avaliacao> optionalAva = repo.findById(id);
		return optionalAva.orElseThrow(() -> new RuntimeException("Avaliação não encontrada com o id: " + id));
	}

	public List<Avaliacao> ListarTodasAvaliacoes() {
		return repo.findAll();
	}
	
	public Avaliacao EditarAvaliacao (Long id, AvaliacaoDTO avDTO) { 
		Avaliacao avaliacaoExistente = LerAvaliacao(id); // Reutiliza o método de busca
		
       // Ao editar, geralmente só atualizamos o conteúdo, não o autor ou o restaurante
		avaliacaoExistente.setTituloAvaliacao(avDTO.getTituloAvaliação());
		avaliacaoExistente.setTextoAvaliacao(avDTO.getTextoAvaliação());
       avaliacaoExistente.setNota(avDTO.getNota()); // <-- Permitir editar a nota também
		
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