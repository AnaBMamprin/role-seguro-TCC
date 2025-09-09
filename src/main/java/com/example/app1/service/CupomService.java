package com.example.app1.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app1.model.Cupom;
import com.example.app1.model.Restaurante;
import com.example.app1.model.Usuario;
import com.example.app1.repository.CupomRepository;
import com.example.app1.repository.RestauranteRepository;
import com.example.app1.repository.UserRepository;

@Service
public class CupomService {

    @Autowired
    private CupomRepository cupomRepository;

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    public Cupom gerarCupom(Long usuarioId, Long restauranteId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

        // regra: só um cupom por semana
        LocalDate inicioSemana = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate fimSemana = LocalDate.now().with(DayOfWeek.SUNDAY);

        boolean jaGerou = cupomRepository.existsByUsuarioAndDataCriacaoBetween(
            usuario, inicioSemana.atStartOfDay(), fimSemana.atTime(LocalTime.MAX)
        );

        if (jaGerou) {
            throw new RuntimeException("Você já gerou um cupom nesta semana!");
        }

        Cupom cupom = new Cupom();
        cupom.setCodigo(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        cupom.setUsuario(usuario);
        cupom.setRestaurante(restaurante);
        cupom.setDataCriacao(LocalDateTime.now());
        cupom.setDesconto(10.0);
        cupom.setUsado(false);

        return cupomRepository.save(cupom);
    }
}
