package com.senai.novo_conta_bancaria.application.service;

import com.senai.novo_conta_bancaria.domain.entity.Taxa;
import com.senai.novo_conta_bancaria.domain.enums.FormaPagamento;
import com.senai.novo_conta_bancaria.domain.repository.TaxaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PagamentoDomainService {
    private final TaxaRepository repository;

    @PreAuthorize("hasRole('CLIENTE')")
    public BigDecimal calcularTaxas(FormaPagamento formaPagamento, BigDecimal valorServico) {
        List<Taxa> taxas = repository.findAllByFormaPagamentoAndAtivoTrue(formaPagamento);

        BigDecimal valorDasTaxas = BigDecimal.valueOf(0);
        for (Taxa taxa : taxas){
            valorDasTaxas = valorDasTaxas.add(taxa
                    .getPercentual()
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                    .multiply(valorServico)
            );
        }

        return valorDasTaxas;
    }
}
