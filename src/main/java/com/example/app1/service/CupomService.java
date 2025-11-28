package com.example.app1.service;

import com.example.app1.model.Cupom;
import com.example.app1.model.Restaurante;
import com.example.app1.model.Usuario;
import com.example.app1.repository.CupomRepository;
import com.example.app1.repository.RestauranteRepository;
import com.example.app1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID; // Para gerar códigos únicos

@Service
public class CupomService {

    @Autowired
    private CupomRepository cupomRepository;

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    public Cupom gerarCupom(Long usuarioId, Long restauranteId) throws RuntimeException {

        boolean jaPossui = cupomRepository.existsByUsuarioIdUsuarioAndRestauranteIdAndTipoCupom(
                usuarioId, restauranteId, "BOAS_VINDAS"
        );

        if (jaPossui) {
            throw new RuntimeException("Você já resgatou o cupom inicial deste restaurante.");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado."));

        Cupom cupom = new Cupom();
        cupom.setUsuario(usuario);
        cupom.setRestaurante(restaurante);
        cupom.setPercentualDesconto(10);
        cupom.setCodigo(gerarCodigoUnico());
        cupom.setTipoCupom("BOAS_VINDAS");
        cupom.setDataCriacao(LocalDateTime.now());
        cupom.setValidade(LocalDateTime.now().plusDays(30));
        cupom.setUsado(false);

        return cupomRepository.save(cupom);
    }

    public Cupom gerarCupomDirecionado(String emailCliente,
                                       Integer percentual,
                                       Restaurante restauranteOfertante,
                                       String tipoCupom)
            throws RuntimeException {

        Usuario clienteAlvo = usuarioRepository.findByEmailUsuario(emailCliente)
                .orElseThrow(() -> new RuntimeException("Não foi encontrado um usuário com o email: " + emailCliente));

        if ("BOAS_VINDAS".equals(tipoCupom)) {
            boolean jaPossui = cupomRepository.existsByUsuarioIdUsuarioAndRestauranteIdAndTipoCupom(
                clienteAlvo.getIdUsuario(), restauranteOfertante.getId(), "BOAS_VINDAS"
            );
            if (jaPossui) {
                 throw new RuntimeException("Esse cliente já possui o cupom de Boas-Vindas.");
            }
        }

        Cupom novoCupom = new Cupom();
        novoCupom.setUsuario(clienteAlvo);
        novoCupom.setRestaurante(restauranteOfertante);
        novoCupom.setPercentualDesconto(percentual);
        novoCupom.setCodigo(gerarCodigoUnico());
        novoCupom.setDataCriacao(LocalDateTime.now());
        novoCupom.setValidade(LocalDateTime.now().plusDays(30));
        novoCupom.setUsado(false);

        return cupomRepository.save(novoCupom);
    }

    private String gerarCodigoUnico() {
    	
        String codigo = UUID.randomUUID().toString().substring(0, 9).toUpperCase();
        return codigo.substring(0, 4) + "-" + codigo.substring(5);
    }
}