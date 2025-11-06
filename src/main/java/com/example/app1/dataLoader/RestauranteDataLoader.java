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

    // O construtor já está correto
    public RestauranteDataLoader(RestauranteRepository repository, RestauranteService service) {
        this.repository = repository;
        this.service = service;
    }
    
    @Override
    public void run(String... args) throws Exception {
        
        // --- CORREÇÃO AQUI ---
        // Agora passamos os dados de endereço "quebrados"
        
        salvarSeNaoExistir(
            "Habibs", "Guarulhos", "SP", "Arabe", "Esfihas, Kibes", "10h às 23h", 
            "Av. Paulo Faccini", "123", "Jardim Maia", // Rua, Numero, Bairro
            "https://habibs.com.br"
        );

        salvarSeNaoExistir(
            "NenoPizzaria", "Guarulhos", "SP", "Italiana", "Massas, Pizzas", "18h às 00h", 
            "Rua Josephina Mandotti", "229", "Jardim Maia", // Rua, Numero, Bairro
            "https://nenopizzaria.com"
        );

        salvarSeNaoExistir(
            "Burger King", "Guarulhos", "SP", "Americana", "Hamburgueres", "11h às 23h", 
            "Av. Paulo Faccini", "1317", "Macedo", // Rua, Numero, Bairro
            "https://burgerking.com.br"
        );

        // (Adicione outros se quiser)
    }

    private void salvarSeNaoExistir(
            String nome, String cidade, String estado, String culinaria,
            String tipodePrato, String horario,
            String rua, String numero, String bairro, String site) {
        
        if (repository.findByNome(nome).isEmpty()) {
            
            // --- CORREÇÃO AQUI ---
            // Agora montamos o DTO da forma correta
            RestauranteDTO dto = new RestauranteDTO();
            dto.setNome(nome);
            dto.setCidade(cidade);
            dto.setEstado(estado);
            dto.setCulinaria(culinaria);
            dto.setTipodeprato(tipodePrato);
            dto.setHorario(horario);
            dto.setSite(site);
            
            // Seta os campos que o Service espera
            dto.setRua(rua);
            dto.setNumero(numero);
            dto.setBairro(bairro);
            
            // Não sete 'dto.setEndereco()', pois não é mais usado aqui

            // Chama o service (que agora vai receber o DTO correto e salvar o mapa)
            service.converteRestaurantes(dto); 
        }
    }
}