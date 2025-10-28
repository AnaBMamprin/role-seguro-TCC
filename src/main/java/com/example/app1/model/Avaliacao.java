package com.example.app1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Avaliacao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idAvaliacao;
	@Column(name = "Titulo_de_avalição")
	private String TituloAvaliacao;
	@Column(name = "Texto_de_avalição")
	private String TextoAvaliacao;
	
	public Avaliacao() {	
	}
	
	public Avaliacao(Long idAvaliacao, String tituloAvaliacao, String textoAvaliacao) {
		
		this.idAvaliacao = idAvaliacao;
		TituloAvaliacao = tituloAvaliacao;
		TextoAvaliacao = textoAvaliacao;
	}

	public Long getIdAvaliacao() {
		return idAvaliacao;
	}

	public void setIdAvaliacao(Long idAvaliacao) {
		this.idAvaliacao = idAvaliacao;
	}

	public String getTituloAvaliacao() {
		return TituloAvaliacao;
	}

	public void setTituloAvaliacao(String tituloAvaliacao) {
		TituloAvaliacao = tituloAvaliacao;
	}

	public String getTextoAvaliacao() {
		return TextoAvaliacao;
	}

	public void setTextoAvaliacao(String textoAvaliacao) {
		TextoAvaliacao = textoAvaliacao;
	}
	
	
	
	
	
	
	

}
