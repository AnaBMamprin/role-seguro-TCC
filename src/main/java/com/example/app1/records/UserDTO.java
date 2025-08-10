package com.example.app1.records;

public class UserDTO {

    private String nome;
    private String email;
    private String endereco;
    private String senha;

    // Construtor vazio
    public UserDTO() {
    }

    // Construtor com parâmetros
    public UserDTO(String nome, String email, String endereco, String senha) {
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
        this.senha = senha;
    }

    // Getters e setters

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
}
