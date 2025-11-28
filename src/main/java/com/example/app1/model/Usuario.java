package com.example.app1.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.app1.usuarioEnums.UserEnum;

import jakarta.persistence.*;

@Entity
@Table(name = "Usuario")
public class Usuario implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Long idUsuario;

    @Column(name = "nomeUsuario")
    private String nomeUsuario;

    @Column(unique = true, name = "emailUsuario")
    private String emailUsuario;

    @Column(name = "EnderecoUsuario")
    private String enderecoUsuario;

    @Column(name = "SenhaUsuario")
    private String senhaUsuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserEnum role = UserEnum.ROLE_USER;
    
    public Usuario() {}
    
    
    
    public Usuario(String nome, String email, String endereco, String senha, UserEnum role) {
        this.nomeUsuario = nome;
        this.emailUsuario = email;
        this.enderecoUsuario = endereco;
        this.senhaUsuario = senha;
        this.role = role;
    }
    
    /*@OneToMany(
            mappedBy = "usuario", // "usuario" é o nome do campo lá em Restaurante.java
            cascade = CascadeType.ALL, // Se deletar o Dono, deleta os restaurantes dele
            orphanRemoval = true // Se remover um restaurante da lista, ele é deletado
        ) 
        private List<Restaurante> restaurantes;  */
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes;

    // Se deletar o usuário, apaga todos os favoritos dele
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorito> favoritos;

    // Getters e Setters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getEnderecoUsuario() {
        return enderecoUsuario;
    }

    public void setEnderecoUsuario(String enderecoUsuario) {
        this.enderecoUsuario = enderecoUsuario;
    }

    public String getSenhaUsuario() {
        return senhaUsuario;
    }

    public void setSenhaUsuario(String senhaUsuario) {
        this.senhaUsuario = senhaUsuario;
    }

    public UserEnum getRole() {
        return role;
    }

    public void setRole(UserEnum role) {
        this.role = role;
    }

    // =========================
    // Spring Security Methods
    // =========================
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getPassword() {
        return this.senhaUsuario;
    }

    @Override
    public String getUsername() {
        return this.emailUsuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
