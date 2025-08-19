package com.example.app1.records;

public class RestauranteDTO {
	
	private String nome;
	private String cidade;
	private String culinaria;
	private String tipodeCardapio;
	
	public RestauranteDTO() {}
	
	

	public RestauranteDTO(String nome, String cidade, String culinaria, String tipodeCardapio) {
		this.nome = nome;
		this.cidade = cidade;
		this.culinaria = culinaria;
		this.tipodeCardapio = tipodeCardapio;
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

	public String getTipodeCardapio() {
		return tipodeCardapio;
	}

	public void setTipodeCardapio(String tipodeCardapio) {
		this.tipodeCardapio = tipodeCardapio;
	}
	
	
	

}
