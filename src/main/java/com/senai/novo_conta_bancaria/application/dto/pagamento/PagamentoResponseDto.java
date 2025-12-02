package com.senai.novo_conta_bancaria.application.dto.pagamento;

import com.senai.novo_conta_bancaria.application.dto.taxa.TaxaResponseDto;
import com.senai.novo_conta_bancaria.domain.entity.Pagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PagamentoResponseDto(
        String id,
        String servico,
        BigDecimal valorServico,
        BigDecimal valorTaxa,
        BigDecimal valorTotal,
        LocalDateTime dataPagamento,
        String status,
        List<TaxaResponseDto> taxas,
        String formaPagamento
) {
        public static PagamentoResponseDto fromEntity(Pagamento pagamento, BigDecimal valorTaxa) {
                List<TaxaResponseDto> taxas = pagamento
                        .getTaxas()
                        .stream()
                        .map(TaxaResponseDto::fromEntity)
                        .toList();
                return new PagamentoResponseDto(
                        pagamento.getId(),
                        pagamento.getServico(),
                        pagamento.getValorPago(),
                        valorTaxa,
                        pagamento.getValorPago().add(valorTaxa),
                        pagamento.getDataPagamento(),
                        pagamento.getStatus().name(),
                        taxas,
                        pagamento.getFormaPagamento().name()
                );
        }
}