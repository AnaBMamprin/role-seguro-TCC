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
        salvarSeNaoExistir("Habibs", "Guarulhos", "Arabe", "Esfihas, Kibes, Tabule");
        salvarSeNaoExistir("NenoPizzaria", "Guarulhos", "Italiana", "Massas, Pizzas");
        salvarSeNaoExistir("Burger King", "Guarulhos", "Americana", "Hamburgueres, Fritas");
        salvarSeNaoExistir("Hermanito Fast-food", "Guarulhos", "Mexicana", "Tacos, Burritos");
        salvarSeNaoExistir("Churrascaria Cumbica", "Guarulhos", "Brasileira", "Churrasco, Feijoada");
        salvarSeNaoExistir("Gendai", "Guarulhos", "Japonesa", "Hot Roll, Uramaki, Temaki");
    }

    private void salvarSeNaoExistir(String nome, String cidade, String culinaria, String tipodecardapio) {
        if (repository.findByNome(nome).isEmpty()) {
            repository.save(new Restaurante(nome, cidade, culinaria, tipodecardapio));
        }
    }
   
}
