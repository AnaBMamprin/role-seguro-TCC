package com.example.app1.records;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRecordDTO(
    @NotBlank(message = "O nome é obrigatório") String nomeLocal,
    @NotBlank(message = "O email é obrigatório") 
    @Email(message = "Email inválido") String emailLocal,
    @NotBlank(message = "O endereço é obrigatório") String enderecoLocal,
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") String senhaLocal
) {}