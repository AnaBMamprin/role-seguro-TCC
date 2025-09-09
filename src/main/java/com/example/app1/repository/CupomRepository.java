package com.example.app1.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app1.model.Cupom;
import com.example.app1.model.Usuario;

public interface CupomRepository extends JpaRepository<Cupom, Long> {
    boolean existsByUsuarioAndDataCriacaoBetween(
        Usuario usuario, LocalDateTime inicio, LocalDateTime fim
    );
}
