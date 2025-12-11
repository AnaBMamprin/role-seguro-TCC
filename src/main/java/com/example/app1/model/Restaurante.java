package com.example.app1.model;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

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
	@Column (name = "Estado")
	private String estado;
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
	private Double latitude;
	@Column (name = "longitude") 
	private Double longitude;
	@Column(name = "caminho_foto")
	private String caminhoFoto;
	@Column(name = "rua")
	private String rua;
	@Column(name = "numero")
	private String numero;
	@Column(name = "cep")
	private String cep;
	@Column(name = "bairro")
	private String bairro;
	@Column (name = "telefone")
	private String telefone;
	
	
/*	@ManyToOne(fetch = FetchType.LAZY) // "MUITOS" Restaurantes para "UM" Usuário
	@JoinColumn(name = "usuario_id", referencedColumnName = "idUsuario") // O nome da sua FK
	private Usuario usuario; */
	
	@OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true) // <--- O SEGREDO ESTÁ AQUI
	private List<Cupom> cupons;
	
	
	public Restaurante() {}
	
	
	public Restaurante( String nome, String cidade, String estado, String culinaria, String tipodeprato,
			String horario, String endereco, String site, Double latitude, Double longitude, String caminhoFoto,
			String rua, String numero, String cep, String bairro, String telefone) {
		super();
		this.nome = nome;
		this.cidade = cidade;
		this.estado = estado;
		this.culinaria = culinaria;
		this.tipodeprato = tipodeprato;
		this.horario = horario;
		this.endereco = endereco;
		this.site = site;
		this.latitude = latitude;
		this.longitude = longitude;
		this.caminhoFoto = caminhoFoto;
		this.rua = rua;
		this.numero = numero;
		this.cep = cep;
		this.bairro = bairro;
		this.telefone = telefone;
	}

	


	public String getCep() {
		return cep;
	}


	public void setCep(String cep) {
		this.cep = cep;
	}


	public String getBairro() {
		return bairro;
	}


	public void setBairro(String bairro) {
		this.bairro = bairro;
	}


	public String getRua() {
		return rua;
	}


	public void setRua(String rua) {
		this.rua = rua;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
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

	public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
    
    
	
}
