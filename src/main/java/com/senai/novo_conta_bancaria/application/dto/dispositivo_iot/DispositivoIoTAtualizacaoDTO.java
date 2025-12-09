package com.senai.novo_conta_bancaria.application.dto.dispositivo_iot;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record DispositivoIoTAtualizacaoDTO(
        @NotNull(message = "O código serial não pode ser nula.")
        @NotBlank(message = "O código serial pode ser vazia.")
        @Size(min = 19, max = 19, message = "O código serial deve ter exatamente 19 caracteres (contando com hífens).")
        String codigoSerial,

        @NotNull(message = "A chave pública não pode ser nula.")
        @NotBlank(message = "A chave pública não pode ser vazia.")
        @Size(min = 3, max = 100, message = "A chave pública deve ter entre 3 e 100 caracteres.")
        String chavePublica,

        @NotNull(message = "O CPF do cliente não pode ser nulo.")
        @Positive(message = "O CPF do cliente não pode ser negativo.")
        @Max(value = 99999999999L, message = "O CPF do cliente deve ter até 11 dígitos.")
        Long clienteCPF
) {
}