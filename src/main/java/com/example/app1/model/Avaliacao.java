package com.example.app1.model;

import java.time.LocalDateTime;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
//--------------------------

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
	
 // Recomendo usar columnDefinition = "TEXT" para textos longos
	@Column(name = "Texto_de_avalição", columnDefinition = "TEXT")
	private String TextoAvaliacao;
	
 // --- CAMPO NOVO: NOTA ---
 @Column(nullable = false) // Uma avaliação deve ter nota
 private int nota; // (Ex: 1, 2, 3, 4, 5)
 
 // --- CAMPO NOVO: DATA ---
 @Column(nullable = false, updatable = false)
 private LocalDateTime dataAvaliacao;
 
 // --- RELACIONAMENTO: USUÁRIO ---
 // Muitas avaliações (@Many) podem ser de Um usuário (@ToOne)
 @ManyToOne(fetch = FetchType.LAZY) // LAZY = só carrega o usuário se você pedir
 @JoinColumn(name = "usuario_id", nullable = false) // Chave estrangeira
 private Usuario usuario;
 
 // --- RELACIONAMENTO: RESTAURANTE ---
 // Muitas avaliações (@Many) podem ser de Um restaurante (@ToOne)
 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "restaurante_id", nullable = false) // Chave estrangeira
 private Restaurante restaurante;
	
 
 // --- CONSTRUTORES ---
	public Avaliacao() {	
	}
	
	
  
 public Avaliacao(Long idAvaliacao, String tituloAvaliacao, String textoAvaliacao, int nota, LocalDateTime dataAvaliacao,
		Usuario usuario, Restaurante restaurante) {
	super();
	this.idAvaliacao = idAvaliacao;
	TituloAvaliacao = tituloAvaliacao;
	TextoAvaliacao = textoAvaliacao;
	this.nota = nota;
	this.dataAvaliacao = dataAvaliacao;
	this.usuario = usuario;
	this.restaurante = restaurante;
}



// --- LÓGICA DA DATA ---
 // Este método é chamado automaticamente ANTES de salvar no banco
 @PrePersist
 protected void onCreate() {
     dataAvaliacao = LocalDateTime.now();
 }
 

	// --- GETTERS E SETTERS ---

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

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}

	public LocalDateTime getDataAvaliacao() {
		return dataAvaliacao;
	}

	public void setDataAvaliacao(LocalDateTime dataAvaliacao) {
		this.dataAvaliacao = dataAvaliacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}
}
