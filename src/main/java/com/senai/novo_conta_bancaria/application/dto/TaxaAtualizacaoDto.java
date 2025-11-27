package com.senai.novo_conta_bancaria.application.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TaxaAtualizacaoDto(
        @NotNull(message = "O percentual não pode ser nulo.")
        @PositiveOrZero(message = "O percentual não pode ser negativo.")
        BigDecimal percentual,

        @NotNull(message = "O valor fixo não pode ser nulo.")
        @PositiveOrZero(message = "O valor fixo não pode ser negativo.")
        BigDecimal valorFixo
) {
}