package com.example.app1.records;

public class RestauranteDTO {
	
	private String nome;
	private String cidade;
	private String culinaria;
	private String tipodePrato;
	private String horario;
	private String endereco;
	private String site;
	
	
	public RestauranteDTO() {}


	public RestauranteDTO(String nome, String cidade, String culinaria, String tipodePrato, String horario,
			String endereco, String site) {
		this.nome = nome;
		this.cidade = cidade;
		this.culinaria = culinaria;
		this.tipodePrato = tipodePrato;
		this.horario = horario;
		this.endereco = endereco;
		this.site = site;
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


	public String getTipodePrato() {
		return tipodePrato;
	}


	public void setTipodePrato(String tipodePrato) {
		this.tipodePrato = tipodePrato;
	}


	public String getHorario() {
		return horario;
	}


	public void setHorario(String horario) {
		this.horario = horario;
	}


	public String getEndereco() {
		return endereco;
	}


	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}


	public String getSite() {
		return site;
	}


	public void setSite(String site) {
		this.site = site;
	}
	
	
	

	
	
	

}
