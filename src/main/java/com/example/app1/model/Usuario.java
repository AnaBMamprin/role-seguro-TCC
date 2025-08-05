package com.example.app1.model;

import java.util.Collection;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Usuario")
public class Usuario implements UserDetails {
	
	 
	private static final long serialVersionUID = 1L;

	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "idUsuario")
	 private Long idLocal;
	
		@Column(name = "nomeUsuario")
		private String nomeLocal;

		@Column(name = "emailUsuario")
		private String emailLocal;

		@Column(name = "EnderecoUsuario")
		private String enderecoLocal;
		
		@Column(name = "SenhaUsuario")
	    private String senhaLocal;

		private String role = "USER";

		
		
		
		public Usuario() {}

		public Usuario( String nome, String email, String endereco, String senha) {
		this.nomeLocal = nome;
		this.emailLocal = email;
		this.senhaLocal = senha;
		this.enderecoLocal = endereco;
	}

		public Long getIdLocal() {
			return idLocal;
		}

		public void setIdLocal(Long idLocal) {
			this.idLocal = idLocal;
		}

		public String getNomeLocal() {
			return nomeLocal;
		}

		public void setNomeLocal(String nomeLocal) {
			this.nomeLocal = nomeLocal;
		}

		public String getEmailLocal() {
			return emailLocal;
		}

		public void setEmailLocal(String emailLocal) {
			this.emailLocal = emailLocal;
		}

		public String getEnderecoLocal() {
			return enderecoLocal;
		}

		public void setEnderecoLocal(String enderecoLocal) {
			this.enderecoLocal = enderecoLocal;
		}

		public String getSenhaLocal() {
			return senhaLocal;
		}

		public void setSenhaLocal(String senhaLocal) {
			this.senhaLocal = senhaLocal;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getPassword() {
			// TODO Auto-generated method stub
			return senhaLocal;
		}

		@Override
		public String getUsername() {
			// TODO Auto-generated method stub
			return emailLocal;
		}
		
		
		

	
	
}
