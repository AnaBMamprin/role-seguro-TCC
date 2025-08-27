package com.example.app1.dataLoader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.app1.model.Restaurante;
import com.example.app1.repository.RestauranteRepository;

@Component
public class RestauranteDataLoader implements CommandLineRunner {

    private final RestauranteRepository repository;

    public RestauranteDataLoader(RestauranteRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        salvarSeNaoExistir("Habibs", "Guarulhos", "Arabe", "Esfihas, Kibes, Tabule",
                "10h às 23h", "Av. Paulo Faccini, 123", "https://habibs.com.br");

        salvarSeNaoExistir("NenoPizzaria", "Guarulhos", "Italiana", "Massas, Pizzas",
                "18h às 00h", "Rua das Pizzas, 456", "https://nenopizzaria.com");

        salvarSeNaoExistir("Burger King", "Guarulhos", "Americana", "Hamburgueres, Fritas",
                "11h às 23h", "Shopping Internacional", "https://burgerking.com.br");

        salvarSeNaoExistir("Hermanito Fast-food", "Guarulhos", "Mexicana", "Tacos, Burritos",
                "12h às 22h", "Av. México, 89", "https://hermanito.com");

        salvarSeNaoExistir("Churrascaria Cumbica", "Guarulhos", "Brasileira", "Churrasco, Feijoada",
                "11h às 23h", "Rod. Presidente Dutra, km 222", "https://churrascariacumbica.com");

        salvarSeNaoExistir("Gendai", "Guarulhos", "Japonesa", "Hot Roll, Uramaki, Temaki",
                "12h às 23h", "Av. Sushi, 321", "https://gendai.com.br");
    }

    private void salvarSeNaoExistir(String nome, String cidade, String culinaria,
                                    String tipodePrato, String horario,
                                    String endereco, String site) {
        if (repository.findByNome(nome).isEmpty()) {
            Restaurante restaurante = new Restaurante(
                    nome, cidade, culinaria, tipodePrato, horario, endereco, site
            );
            repository.save(restaurante);
        }
    }
   
}
