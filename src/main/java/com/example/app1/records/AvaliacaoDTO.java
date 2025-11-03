package com.example.app1.records;

public class AvaliacaoDTO {
	
	private Long id;
	private String tituloAvaliação;
	private String textoAvaliação;
	private int nota; 
    private Long usuarioId; 
    private Long restauranteId;
	
	public AvaliacaoDTO(){} 
	

	public AvaliacaoDTO(Long id, String tituloAvaliação, String textoAvaliação, int nota, Long usuarioId,
			Long restauranteId) {
		super();
		this.id = id;
		this.tituloAvaliação = tituloAvaliação;
		this.textoAvaliação = textoAvaliação;
		this.nota = nota;
		this.usuarioId = usuarioId;
		this.restauranteId = restauranteId;
	}
	
	

	public int getNota() {
		return nota;
	}


	public void setNota(int nota) {
		this.nota = nota;
	}


	public Long getUsuarioId() {
		return usuarioId;
	}


	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}


	public Long getRestauranteId() {
		return restauranteId;
	}


	public void setRestauranteId(Long restauranteId) {
		this.restauranteId = restauranteId;
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
