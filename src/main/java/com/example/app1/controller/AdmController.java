package com.example.app1.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import com.example.app1.model.Restaurante;
import com.example.app1.model.Usuario;
import com.example.app1.records.RestauranteDTO;
import com.example.app1.repository.AvaliacaoRepository;
import com.example.app1.repository.FavoritoRepository;
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
    private FavoritoRepository favoritoRepository;
    
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;
    
    @Autowired
    private RestauranteService restauranteService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    
    
    @GetMapping
    public String paginaAdm(Model model) {

        List<Restaurante> restaurantes = restauranteRepository.findAll(
            Sort.by(Sort.Direction.ASC, "nome")
        );
        
        List<Usuario> usuarios = userRepository.findAll(
            Sort.by(Sort.Direction.ASC, "nomeUsuario")
        );
        
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
    
    @PostMapping("/avaliacaoExcluir")
    public String excluirAvaliacao(
            @RequestParam("id") Long id, 
            @RequestParam("restauranteId") Long restauranteId,
            RedirectAttributes redirectAttributes) {
        
        try {
            avaliacaoRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("sucesso", "Avaliação removida com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao remover avaliação.");
        }
        
        return "redirect:/modelo-restaurante?id=" + restauranteId;
    }
    
   

    @PostMapping("/restauranteCadastrar")
    public String cadastrarRestaurante(
            @ModelAttribute RestauranteDTO dto,
            @RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile, RedirectAttributes redirectAttributes
        ) { 
    	
    	System.out.println("--- [ADM CONTROLLER] INICIANDO CADASTRO ---");

        try {
            if (fotoFile != null && !fotoFile.isEmpty()) {
            	
            	System.out.println("[ADM CONTROLLER] Foto recebida: " + fotoFile.getOriginalFilename());
            	
                String nomeArquivo = fileStorageService.salvarFotoRestaurante(fotoFile);
                
                System.out.println("[ADM CONTROLLER] Foto salva no servidor com nome: " + nomeArquivo);
                
                dto.setCaminhoFoto(nomeArquivo); 
                
                System.out.println("[ADM CONTROLLER] DTO agora tem a foto: " + dto.getCaminhoFoto());
                
            } else {
                System.out.println("[ADM CONTROLLER] Nenhuma foto foi enviada no cadastro.");
            }

            restauranteService.salvarRestaurante(dto); 
            
            redirectAttributes.addFlashAttribute("sucesso", "Restaurante cadastrado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar restaurante: " + e.getMessage());
        }
        
        System.out.println("--- [ADM CONTROLLER] FIM CADASTRO ---");
        return "redirect:/adm";
    }
    
    @PostMapping("/restauranteEditar")
	public String editarRestaurante(
	        @ModelAttribute RestauranteDTO dto,
	        @RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile,
	        RedirectAttributes redirectAttributes) { 
	    
	    Long idDoRestaurante = dto.getId();
	    
	    System.out.println("--- [ADM CONTROLLER] INICIANDO EDIÇÃO (ID: " + idDoRestaurante + ") ---"); 

	    try {
	        if (fotoFile != null && !fotoFile.isEmpty()) {
	            
	            System.out.println("[ADM CONTROLLER] (EDIÇÃO) Foto recebida: " + fotoFile.getOriginalFilename());
	            
	            String nomeArquivo = fileStorageService.salvarFotoRestaurante(fotoFile);
	            
	            System.out.println("[ADM CONTROLLER] (EDIÇÃO) Foto salva no servidor com nome: " + nomeArquivo);
	            
	            dto.setCaminhoFoto(nomeArquivo);

	        } else {
	            System.out.println("[ADM CONTROLLER] (EDIÇÃO) Nenhuma foto nova foi enviada.");
	        }
	        
	        restauranteService.atualizarRestaurante(idDoRestaurante, dto);
	        
	        redirectAttributes.addFlashAttribute("sucesso", "Restaurante atualizado com sucesso!");

	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("erro", "Erro ao editar restaurante: " + e.getMessage());
	    }

	    System.out.println("--- [ADM CONTROLLER] FIM EDIÇÃO ---");
	    return "redirect:/adm";
	}

    @PostMapping("/restauranteExcluir")
    public String excluirRestaurante(@RequestParam("id") Long id, RedirectAttributes redirect) {
        try {
            Restaurante restaurante = restauranteRepository.findById(id).orElse(null);
            
            if (restaurante != null) {
                favoritoRepository.deleteByRestaurante(restaurante);
                
                avaliacaoRepository.deleteByRestaurante(restaurante);

                restauranteRepository.delete(restaurante);
                
                redirect.addFlashAttribute("sucesso", "Restaurante e seus dados vinculados foram excluídos!");
            } else {
                redirect.addFlashAttribute("erro", "Restaurante não encontrado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            redirect.addFlashAttribute("erro", "Erro ao excluir restaurante: " + e.getMessage());
        }
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
                             @RequestParam("role") String roleName,
                             RedirectAttributes redirectAttributes) {

        try {
            Usuario usuario = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            UserEnum novoRole = UserEnum.valueOf(roleName);

            if (usuario.getRole() == UserEnum.ROLE_ADMIN && novoRole != UserEnum.ROLE_ADMIN) {
                long totalAdmins = userRepository.countByRole(UserEnum.ROLE_ADMIN);
                if (totalAdmins <= 1) {
                    throw new RuntimeException("Não é possível rebaixar o único administrador do sistema.");
                }
            }

            usuario.setNomeUsuario(nome);
            usuario.setEmailUsuario(email);
            usuario.setEnderecoUsuario(endereco);
            usuario.setRole(novoRole);

            userRepository.save(usuario);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário " + nome + " atualizado com sucesso!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar usuário: " + e.getMessage());
        }

        return "redirect:/adm";
    }


}
    
