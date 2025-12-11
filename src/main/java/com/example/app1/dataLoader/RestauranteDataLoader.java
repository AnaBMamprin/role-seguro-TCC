package com.example.app1.dataLoader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.app1.records.RestauranteDTO;
import com.example.app1.repository.RestauranteRepository;
import com.example.app1.service.RestauranteService;

@Component
public class RestauranteDataLoader implements CommandLineRunner {

    private final RestauranteRepository repository;
    private final RestauranteService service;

    public RestauranteDataLoader(RestauranteRepository repository, RestauranteService service) {
        this.repository = repository;
        this.service = service;
    }
    
    @Override
    public void run(String... args) throws Exception {

        salvarSeNaoExistir(
            "NenoPizzaria", "Guarulhos", "SP", "Pizzas", "Rodízio, A La Carte", "18h às 00h", 
            "Rua Josephina Mandotti", "229", "Jardim Maia",
            "https://nenopizzaria.com", "1199999-9999"
        );

    }

    private void salvarSeNaoExistir(
            String nome, String cidade, String estado, String culinaria,
            String tipodePrato, String horario,
            String rua, String numero, String bairro, String site, String telefone) {
        
        if (repository.findByNome(nome).isEmpty()) {
            
            RestauranteDTO dto = new RestauranteDTO();
            dto.setNome(nome);
            dto.setCidade(cidade);
            dto.setEstado(estado);
            dto.setCulinaria(culinaria);
            dto.setTipodeprato(tipodePrato);
            dto.setHorario(horario);
            dto.setSite(site);
            
            dto.setRua(rua);
            dto.setNumero(numero);
            dto.setBairro(bairro);
            dto.setTelefone(telefone);
            
            service.converteRestaurantes(dto); 
        }
    }
}