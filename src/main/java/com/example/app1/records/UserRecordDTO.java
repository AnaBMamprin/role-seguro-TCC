package com.example.app1.records;

import jakarta.validation.constraints.NotBlank;

public record UserRecordDTO(@NotBlank String nome,@NotBlank String email, @NotBlank String senha, @NotBlank String endereco) {

}
