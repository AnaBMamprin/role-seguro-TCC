package com.example.app1.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    // 1. Define o caminho da pasta que criamos no Passo 1.1
    // Usamos "file:./" para indicar o caminho relativo da raiz do projeto
    private final Path uploadDir = Paths.get("src/main/resources/static/uploads/restaurante-fotos");

    public FileStorageService() {
        try {
            // Cria o diretório se ele não existir
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de uploads!", e);
        }
    }

    public String salvarFotoRestaurante(MultipartFile file) {
        if (file.isEmpty()) {
            return null; // Nenhum arquivo enviado
        }

        try {
            // 2. Gera um nome de arquivo único para evitar sobreposição
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // 3. Resolve o caminho completo do arquivo (diretório + nome)
            Path destinationFile = this.uploadDir.resolve(uniqueFilename);

            // 4. Copia o arquivo enviado para o nosso diretório de destino
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // 5. Retorna APENAS o nome único do arquivo para salvar no banco
            return uniqueFilename;

        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar o arquivo: " + e.getMessage(), e);
        }
    }
    
    // (Opcional) Adicione um método para deletar a foto antiga aqui
}
