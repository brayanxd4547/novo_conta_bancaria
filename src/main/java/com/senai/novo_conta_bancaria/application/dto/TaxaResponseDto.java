package com.senai.novo_conta_bancaria.application.dto;

import com.senai.novo_conta_bancaria.domain.entity.Taxa;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TaxaResponseDto(
        String id,

        @NotNull(message = "A descrição não pode ser nula.")
        @NotBlank(message = "A descrição não pode ser vazia.")
        @Size(min = 3, max = 100, message = "A descrição deve ter entre 3 e 100 caracteres.")
        String descricao,

        @NotNull(message = "O percentual não pode ser nulo.")
        @PositiveOrZero(message = "O percentual não pode ser negativo.")
        BigDecimal percentual,

        @NotNull(message = "O valor fixo não pode ser nulo.")
        @PositiveOrZero(message = "O valor fixo não pode ser negativo.")
        BigDecimal valorFixo
) {
    public static TaxaResponseDto fromEntity(Taxa taxa) {
        return new TaxaResponseDto(
                taxa.getId(),
                taxa.getDescricao(),
                taxa.getPercentual(),
                taxa.getValorFixo()
        );
    }
}