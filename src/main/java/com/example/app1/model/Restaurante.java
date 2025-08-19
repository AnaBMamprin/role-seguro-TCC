package com.example.app1.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Restaurante {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY) 
	
	@Column (name = "id")
	private long id;
	@Column (name = "nome")
	private String nome;
	@Column (name = "cidade")
	private String cidade;
	@Column (name = "culinaria")
	private String culinaria;
	@Column  (name = "tipoDecardapio")
	private String tipoDecardapio;
	
	
	public Restaurante() {}
	
	
	
	
	
	
	public Restaurante(String nome, String cidade, String culinaria, String tipoDecardapio) {
		
		this.nome = nome;
		this.cidade = cidade;
		this.culinaria = culinaria;
		this.tipoDecardapio = tipoDecardapio;
	}






	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getCulinaria() {
		return culinaria;
	}
	public void setCulinaria(String culinaria) {
		this.culinaria = culinaria;
	}
	public String getTipodecardapio() {
		return tipoDecardapio;
	}
	public void setTipodecardapio(String tipodecardapio) {
		tipoDecardapio = tipodecardapio;
	}
	
	
	
	

}
