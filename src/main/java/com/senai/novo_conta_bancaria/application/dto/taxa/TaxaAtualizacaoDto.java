package com.senai.novo_conta_bancaria.application.dto.taxa;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TaxaAtualizacaoDto(
        @NotNull(message = "A descrição não pode ser nula.")
        @NotBlank(message = "A descrição não pode ser vazia.")
        @Size(min = 3, max = 100, message = "A descrição deve ter entre 3 e 100 caracteres.")
        String descricao,

        @NotNull(message = "O percentual não pode ser nulo.")
        @PositiveOrZero(message = "O percentual não pode ser negativo.")
        BigDecimal percentual,

        @NotNull(message = "O valor fixo não pode ser nulo.")
        @PositiveOrZero(message = "O valor fixo não pode ser negativo.")
        BigDecimal valorFixo,

        @NotNull(message = "A forma de pagamento taxada não pode ser nula.")
        @NotBlank(message = "A forma de pagamento taxada não pode ser vazia.")
        @Size(min = 3, max = 100, message = "A forma de pagamento taxada deve ter entre 3 e 100 caracteres.")
        String formaPagamento
) {
}