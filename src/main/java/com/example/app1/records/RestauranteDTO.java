package com.example.app1.records;

public class RestauranteDTO {
	
	private String nome;
	private String cidade;
	private String culinaria;
	private String tipodeprato;
	private String horario;
	private String endereco;
	private String site;
	private Long id;
	private String email;
	private String senha;
	private String caminhoFoto;
	
	
	public RestauranteDTO() {}
	
	
	
	public RestauranteDTO(String nome, String cidade, String culinaria, String tipodeprato, String horario,
			String endereco, String site, Long id, String email, String senha, String caminhoFoto) {
		
		this.nome = nome;
		this.cidade = cidade;
		this.culinaria = culinaria;
		this.tipodeprato = tipodeprato;
		this.horario = horario;
		this.endereco = endereco;
		this.site = site;
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.caminhoFoto = caminhoFoto;
	}

	public String getCaminhoFoto() {
		return caminhoFoto;
	}



	public void setCaminhoFoto(String caminhoFoto) {
		this.caminhoFoto = caminhoFoto;
	}



	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getSenha() {
		return senha;
	}


	public void setSenha(String senha) {
		this.senha = senha;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
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


	public String getTipodeprato() {
		return tipodeprato;
	}


	public void setTipodeprato(String tipodeprato) {
		this.tipodeprato = tipodeprato;
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
