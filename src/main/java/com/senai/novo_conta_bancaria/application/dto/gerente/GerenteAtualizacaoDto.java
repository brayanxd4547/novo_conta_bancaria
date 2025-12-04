package com.senai.novo_conta_bancaria.application.dto.gerente;

import jakarta.validation.constraints.*;

public record GerenteAtualizacaoDto(
        @NotNull(message = "O nome não pode ser nulo.")
        @NotBlank(message = "O nome não pode ser vazio.")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        String nome,

        @Email
        @NotNull(message = "O e-mail não pode ser nulo.")
        @NotBlank(message = "O e-mail não pode ser vazio.")
        String email,

        @NotNull(message = "A senha não pode ser nula.")
        @NotBlank(message = "A senha não pode ser vazia.")
        @Size(min = 8, max = 100, message = "A senha deve ter entre 8 e 100 caracteres.")
        String senha
) {
}