package com.senai.novo_conta_bancaria.application.dto;

import com.senai.novo_conta_bancaria.domain.entity.Taxa;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TaxaRegistroDto(
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
    public Taxa toEntity() {
        return Taxa.builder()
                .ativo(true)
                .descricao(descricao)
                .percentual(percentual)
                .valorFixo(valorFixo)
                .build();
    }
}