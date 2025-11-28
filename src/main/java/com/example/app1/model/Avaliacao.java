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
	
	@Column(name = "Texto_de_avalição", columnDefinition = "TEXT")
	private String TextoAvaliacao;

 @Column(nullable = false)
 private Integer nota;

 @Column(nullable = false, updatable = false, name = "data_avaliacao")
 private LocalDateTime dataAvaliacao;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "usuario_id", nullable = false)
 private Usuario usuario;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "restaurante_id", nullable = false)
 private Restaurante restaurante;
	
 
	public Avaliacao() {	
	}
	
 public Avaliacao(Long idAvaliacao, String tituloAvaliacao, String textoAvaliacao, Integer nota, LocalDateTime dataAvaliacao,
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

 @PrePersist
 protected void onCreate() {
     dataAvaliacao = LocalDateTime.now();
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

	public Integer getNota() {
		return nota;
	}

	public void setNota(Integer nota) {
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
