package com.example.app1.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.app1.model.Restaurante;
import com.example.app1.model.Usuario;
import com.example.app1.records.RestauranteDTO;
import com.example.app1.repository.RestauranteRepository;
import com.example.app1.repository.UserRepository;
import com.example.app1.service.FileStorageService;
import com.example.app1.service.RestauranteService;
import com.example.app1.usuarioEnums.UserEnum;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.transaction.Transactional;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/adm")
public class AdmController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestauranteService restauranteService;
    
    @Autowired
    private FileStorageService fileStorageService;

    // ================== RESTAURANTES ==================
    
    @GetMapping
    public String paginaAdm(Model model) {
        List<Restaurante> restaurantes = restauranteRepository.findAll();
        List<Usuario> usuarios = userRepository.findAll();
        long totalAdmins = usuarios.stream()
               .filter(u -> u.getRole() == UserEnum.ROLE_ADMIN)
               .count();
        List<String> culinarias = restauranteRepository.findDistinctCulinarias();
        model.addAttribute("restaurantes", restaurantes);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalAdmins", totalAdmins);
        model.addAttribute("culinariasUnicas", culinarias);
        return "adm";
    }
    
   

    @PostMapping("/restauranteCadastrar")
    public String cadastrarRestaurante(
            @ModelAttribute RestauranteDTO dto,
            // 1. Receba o ID do Dono do <select> que está no formulário
            @RequestParam("idDoUsuarioDono") Long idDono, 
            @RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile, RedirectAttributes redirectAttributes
        ) { 
    	
    	System.out.println("--- [ADM CONTROLLER] INICIANDO CADASTRO ---");

        try {
            // 3. Verifica se a foto foi enviada
            if (fotoFile != null && !fotoFile.isEmpty()) {
            	
            	System.out.println("[ADM CONTROLLER] Foto recebida: " + fotoFile.getOriginalFilename());
            	
                // 4. Salva a foto no servidor
                String nomeArquivo = fileStorageService.salvarFotoRestaurante(fotoFile);
                
                System.out.println("[ADM CONTROLLER] Foto salva no servidor com nome: " + nomeArquivo); // <-- PONTO CRÍTICO 1
                
                // 5. Coloca o nome do arquivo no DTO
                dto.setCaminhoFoto(nomeArquivo); 
                
                System.out.println("[ADM CONTROLLER] DTO agora tem a foto: " + dto.getCaminhoFoto()); // <-- PONTO CRÍTICO 2
                
            } else {
                System.out.println("[ADM CONTROLLER] Nenhuma foto foi enviada no cadastro.");
            }

            // 6. Manda o DTO (com ou sem foto) para o service
            restauranteService.salvarRestaurante(dto, idDono); 
            
            redirectAttributes.addFlashAttribute("sucesso", "Restaurante cadastrado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar restaurante: " + e.getMessage());
        }
        
        System.out.println("--- [ADM CONTROLLER] FIM CADASTRO ---");
        return "redirect:/adm";
    }
    
            // 3. Verifica se uma NOVA foto foi enviada
        	@PostMapping("/restauranteEditar")
        public String editarRestaurante(
            @ModelAttribute RestauranteDTO dto, 
            @RequestParam("id") Long id,
            @RequestParam(value = "idDoUsuarioDono", required = false) Long idDono,
            @RequestParam("fotoFile") MultipartFile fotoFile,
            RedirectAttributes redirectAttributes) { 
        	    
        	    System.out.println("--- [ADM CONTROLLER] INICIANDO EDIÇÃO (ID: " + id + ") ---"); // <-- ADICIONE

        	    try {
        	        // 3. Verifica se uma NOVA foto foi enviada
        	        if (fotoFile != null && !fotoFile.isEmpty()) {
        	            
        	            System.out.println("[ADM CONTROLLER] (EDIÇÃO) Foto recebida: " + fotoFile.getOriginalFilename()); // <-- ADICIONE
        	            
        	            // 4. Salva a nova foto
        	            String nomeArquivo = fileStorageService.salvarFotoRestaurante(fotoFile);
        	            
        	            System.out.println("[ADM CONTROLLER] (EDIÇÃO) Foto salva no servidor com nome: " + nomeArquivo); // <-- ADICIONE
        	            
        	            // 5. Coloca o nome no DTO (o service vai saber o que fazer)
        	            dto.setCaminhoFoto(nomeArquivo);

        	        } else {
        	            System.out.println("[ADM CONTROLLER] (EDIÇÃO) Nenhuma foto nova foi enviada."); // <-- ADICIONE
        	        }
        	        
                    // 6. Manda para o service atualizar (passando id do dono se houver)
                    restauranteService.atualizarRestaurante(id, dto, idDono);
        	        
        	        redirectAttributes.addFlashAttribute("sucesso", "Restaurante atualizado com sucesso!");

        	    } catch (Exception e) {
        	        e.printStackTrace();
        	        redirectAttributes.addFlashAttribute("erro", "Erro ao editar restaurante: " + e.getMessage());
        	    }

        	    System.out.println("--- [ADM CONTROLLER] FIM EDIÇÃO ---"); // <-- ADICIONE
        	    return "redirect:/adm";
        	}

    @PostMapping("/restauranteExcluir")
    public String excluirRestaurante(@RequestParam("id") Long id) {
        restauranteRepository.deleteById(id);
        return "redirect:/adm";
    }

    // ================== USUÁRIOS ==================

/*    @PostMapping("/usuarioToggleAdm")
    public String toggleAdm(@RequestParam("id") Long id) {
        // ... (seu método está OK)
        Usuario usuario = userRepository.findById(id).orElse(null);
        if (usuario != null) {
            if (usuario.getRole() == UserEnum.ROLE_USER) {
                usuario.setRole(UserEnum.ROLE_ADMIN);
            } else {
                usuario.setRole(UserEnum.ROLE_USER);
            }
            userRepository.save(usuario);
        }
        return "redirect:/adm";
    } */
    
    
    @PostMapping("/updateUser")
    @Transactional
    public String updateUser(@RequestParam("id") Long id,
                             @RequestParam("nome") String nome,
                             @RequestParam("email") String email,
                             @RequestParam("endereco") String endereco,
                             @RequestParam("role") String roleName, // <-- Recebe a String do <select>
                             RedirectAttributes redirectAttributes) {

        try {
            Usuario usuario = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            UserEnum novoRole = UserEnum.valueOf(roleName); // Converte a String (ex: "ROLE_RESTAURANTE") para o Enum

            // Lógica de proteção do último admin
            if (usuario.getRole() == UserEnum.ROLE_ADMIN && novoRole != UserEnum.ROLE_ADMIN) {
                // Se o usuário ATUAL é ADMIN e o NOVO papel NÃO é ADMIN (ou seja, um rebaixamento)
                long totalAdmins = userRepository.countByRole(UserEnum.ROLE_ADMIN);
                if (totalAdmins <= 1) {
                    // Impede o rebaixamento
                    throw new RuntimeException("Não é possível rebaixar o único administrador do sistema.");
                }
            }

            // Atualiza os dados
            usuario.setNomeUsuario(nome);
            usuario.setEmailUsuario(email);
            usuario.setEnderecoUsuario(endereco);
            usuario.setRole(novoRole); // Define o novo papel

            userRepository.save(usuario);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário " + nome + " atualizado com sucesso!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar usuário: " + e.getMessage());
        }

        return "redirect:/adm";
    }


}
    
