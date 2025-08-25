package com.example.app1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "favoritos")
public class Favorito {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne
	    @JoinColumn(name = "usuario_id")
	    private Usuario usuario;

	    @ManyToOne
	    @JoinColumn(name = "restaurante_id")
	    private Restaurante restaurante;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
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


