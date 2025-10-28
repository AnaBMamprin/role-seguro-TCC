package com.example.app1.records;

public class AvaliacaoDTO {
	
	private Long id;
	private String tituloAvaliação;
	private String textoAvaliação;
	
	public AvaliacaoDTO(){} 
	
	public AvaliacaoDTO(String tituloAvaliação, String textoAvaliação) {
		super();
		this.tituloAvaliação = tituloAvaliação;
		this.textoAvaliação = textoAvaliação;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getTituloAvaliação() {
		return this.tituloAvaliação;
	}


	public void setTituloAvaliação(String tituloAvaliação) {
		this.tituloAvaliação = tituloAvaliação;
	}


	public String getTextoAvaliação() {
		return this.textoAvaliação;
	}


	public void setTextoAvaliação(String textoAvaliação) {
		this.textoAvaliação = textoAvaliação;
	}
	
	
	
	
	
	
	
}
