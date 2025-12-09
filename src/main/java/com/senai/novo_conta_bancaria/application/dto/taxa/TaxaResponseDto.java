package com.senai.novo_conta_bancaria.application.dto.taxa;

import com.senai.novo_conta_bancaria.domain.entity.Taxa;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TaxaResponseDto(
        String id,
        String descricao,
        BigDecimal percentual,
        BigDecimal valorFixo,
        String formaPagamento
) {
    public static TaxaResponseDto fromEntity(Taxa taxa) {
        return new TaxaResponseDto(
                taxa.getId(),
                taxa.getDescricao(),
                taxa.getPercentual(),
                taxa.getValorFixo(),
                taxa.getFormaPagamento().name()
        );
    }
}