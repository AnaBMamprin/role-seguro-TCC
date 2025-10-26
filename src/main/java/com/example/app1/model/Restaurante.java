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
	private Long id;
	
	@Column (name = "nome")
	private String nome;
	@Column (name = "cidade")
	private String cidade;


	@Column (name = "culinaria")
	private String culinaria;
	@Column  (name = "tipodeprato")
	private String tipodeprato;
	@Column  (name = "horario")
	private String horario;
	@Column  (name = "endereco")
	private String endereco;
	@Column  (name = "site")
	private String site;
	@Column (name = "latitude") 
	private double latitude;
	@Column (name = "longitude") 
	private double longitude;
	
	
	public Restaurante() {}
	

	public Restaurante( String nome, String cidade, String culinaria, String tipodeprato, String horario,
			String endereco, String site) {
		this.nome = nome;
		this.cidade = cidade;
		this.culinaria = culinaria;
		this.tipodeprato = tipodeprato;
		this.horario = horario;
		this.endereco = endereco;
		this.site = site;
	}

	public long getId() {
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

	public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
	
}
