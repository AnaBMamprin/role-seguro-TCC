package com.example.app1.dataLoader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.app1.model.Restaurante;
import com.example.app1.records.RestauranteDTO;
import com.example.app1.repository.RestauranteRepository;
import com.example.app1.service.RestauranteService;

@Component
public class RestauranteDataLoader implements CommandLineRunner {

    private final RestauranteRepository repository;
    private final RestauranteService service;

    public RestauranteDataLoader(RestauranteRepository repository, RestauranteService service) {
        this.repository = repository;
        this.service = service; // 2.
    }
    
    @Override
    public void run(String... args) throws Exception {
        salvarSeNaoExistir("Habibs", "Guarulhos", "Arabe", "Esfihas, Kibes, Tabule",
                "10h às 23h", "Av. Paulo Faccini, 123", "https://habibs.com.br", "São Paulo");

        salvarSeNaoExistir("NenoPizzaria", "Guarulhos", "Italiana", "Massas, Pizzas",
                "18h às 00h", "Rua das Pizzas, 456", "https://nenopizzaria.com" , "São Paulo");

        salvarSeNaoExistir("Burger King", "Guarulhos", "Americana", "Hamburgueres, Fritas",
                "11h às 23h", "Shopping Internacional", "https://burgerking.com.br", "São Paulo");

        salvarSeNaoExistir("Hermanito Fast-food", "Guarulhos", "Mexicana", "Tacos, Burritos",
                "12h às 22h", "Av. México, 89", "https://hermanito.com", "São Paulo");

        salvarSeNaoExistir("Churrascaria Cumbica", "Guarulhos", "Brasileira", "Churrasco, Feijoada",
                "11h às 23h", "Rod. Presidente Dutra, km 222", "https://churrascariacumbica.com", "São Paulo");

        salvarSeNaoExistir("Gendai", "Guarulhos", "Japonesa", "Hot Roll, Uramaki, Temaki",
                "12h às 23h", "Av. Sushi, 321", "https://gendai.com.br", "São Paulo");
    }

    private void salvarSeNaoExistir(String nome, String cidade, String culinaria,
                                    String tipodePrato, String horario,
                                    String endereco, String site, String estado) {
if (repository.findByNome(nome).isEmpty()) {
            
            RestauranteDTO dto = new RestauranteDTO();
            dto.setNome(nome);
            dto.setCidade(cidade);
            dto.setEstado(estado);
            dto.setEndereco(endereco);
            dto.setCulinaria(culinaria);
            dto.setTipodeprato(tipodePrato);
            dto.setHorario(horario);
            dto.setSite(site);

            service.converteRestaurantes(dto); 
        }
    }
   
}
