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

    // =========================================================================
    // === FLUXO 1: CLIENTE PEGA CUPOM (O seu método antigo) ===
    // =========================================================================

    /**
     * Chamado quando o CLIENTE logado clica para pegar um cupom
     * (Ex: cupom de "Boas-Vindas").
     */
    public Cupom gerarCupom(Long usuarioId, Long restauranteId) throws RuntimeException {

        // 1. Regra: Verifica se o cliente já pegou o cupom de "BOAS_VINDAS"
        // (Isso exige o método no CupomRepository que discutimos)
        boolean jaPossui = cupomRepository.existsByUsuarioIdUsuarioAndRestauranteIdAndTipoCupom(
                usuarioId, restauranteId, "BOAS_VINDAS"
        );

        if (jaPossui) {
            throw new RuntimeException("Você já resgatou o cupom inicial deste restaurante.");
        }

        // 2. Busca as entidades
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado."));

        // 3. Cria o cupom de Boas-Vindas
        Cupom cupom = new Cupom();
        cupom.setUsuario(usuario);
        cupom.setRestaurante(restaurante);
        cupom.setPercentualDesconto(10); // Ex: Fixo de 10%
        cupom.setCodigo(gerarCodigoUnico());
        cupom.setTipoCupom("BOAS_VINDAS");
        cupom.setDataCriacao(LocalDateTime.now());
        cupom.setValidade(LocalDateTime.now().plusDays(30)); // Ex: Vale por 30 dias
        cupom.setUsado(false);

        return cupomRepository.save(cupom);
    }

    // =========================================================================
    // === FLUXO 2: RESTAURANTE GERA CUPOM (O seu método novo) ===
    // =========================================================================

    /**
     * Chamado quando o DONO DO RESTAURANTE preenche o formulário
     * para dar um cupom a um cliente específico via e-mail.
     */
    public Cupom gerarCupomDirecionado(String emailCliente,
                                       Integer percentual,
                                       Restaurante restauranteOfertante,
                                       String tipoCupom)
            throws RuntimeException {

        // 1. Regra: Validar se o e-mail do cliente existe no banco
        // (Seu UserRepository deve ter o findByEmailUsuario)
        Usuario clienteAlvo = usuarioRepository.findByEmailUsuario(emailCliente)
                .orElseThrow(() -> new RuntimeException("Não foi encontrado um usuário com o email: " + emailCliente));

        // 2. Regra (Opcional): Se o tipo for "BOAS_VINDAS", checar se já não tem
        if ("BOAS_VINDAS".equals(tipoCupom)) {
            boolean jaPossui = cupomRepository.existsByUsuarioIdUsuarioAndRestauranteIdAndTipoCupom(
                clienteAlvo.getIdUsuario(), restauranteOfertante.getId(), "BOAS_VINDAS"
            );
            if (jaPossui) {
                 throw new RuntimeException("Esse cliente já possui o cupom de Boas-Vindas.");
            }
        }
        
        // 3. Cria o cupom
        Cupom novoCupom = new Cupom();
        novoCupom.setUsuario(clienteAlvo); // O cliente encontrado
        novoCupom.setRestaurante(restauranteOfertante); // O restaurante do dono logado
        novoCupom.setPercentualDesconto(percentual); // O % vindo do formulário
        novoCupom.setCodigo(gerarCodigoUnico());
        novoCupom.setDataCriacao(LocalDateTime.now());
        novoCupom.setValidade(LocalDateTime.now().plusDays(30)); // Ex: Vale por 30 dias
        novoCupom.setUsado(false);

        return cupomRepository.save(novoCupom);
    }


    // =========================================================================
    // === MÉTODO PRIVADO (Helper) ===
    // =========================================================================

    /**
     * Gera um código único para o cupom.
     */
    private String gerarCodigoUnico() {
        // Pega os primeiros 8 caracteres de um UUID e coloca em maiúsculo
        // Ex: A1B2-C3D4
        String codigo = UUID.randomUUID().toString().substring(0, 9).toUpperCase();
        return codigo.substring(0, 4) + "-" + codigo.substring(5);
    }
}