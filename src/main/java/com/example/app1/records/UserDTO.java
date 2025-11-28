package com.example.app1.records;

public class UserDTO {

	private Long id;
    private String nome;
    private String email;
    private String endereco;
    private String senha;
    private boolean admin;

    public UserDTO() {
    }

    public UserDTO(Long id, String nome, String email, String endereco, String senha, boolean admin) {
    	this.id = id;
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
        this.senha = senha;
        this.admin = admin;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public boolean isAdmin() { 
    	return admin; 
    }
    
    public void setAdmin(boolean admin) { 
    	this.admin = admin; 
    }
}
