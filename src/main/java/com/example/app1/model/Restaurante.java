package com.example.app1.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "Usuario_id", referencedColumnName = "idUsuario") // IMPORTANTE: Verifique o nome da PK do seu UsuarioModel
    private Usuario usuario;
	
	
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
	
	

	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
	
}
