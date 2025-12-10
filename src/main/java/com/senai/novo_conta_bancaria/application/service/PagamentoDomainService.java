package com.senai.novo_conta_bancaria.application.service;

import com.senai.novo_conta_bancaria.domain.entity.Conta;
import com.senai.novo_conta_bancaria.domain.entity.Taxa;
import com.senai.novo_conta_bancaria.domain.enums.FormaPagamento;
import com.senai.novo_conta_bancaria.domain.exception.FormaDePagamentoInvalidaException;
import com.senai.novo_conta_bancaria.domain.exception.SaldoInsuficienteException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class PagamentoDomainService {
    private final ContaService contaService;

    @PreAuthorize("hasRole('CLIENTE')")
    public FormaPagamento validarFormaPagamento(String formaPagamento){
        boolean formaPagamentoExiste = false;
        for(FormaPagamento formaPossivel : FormaPagamento.values()) {
            if (formaPagamento.equals(formaPossivel.name())) {
                formaPagamentoExiste = true;
                break;
            }
        }
        if(!formaPagamentoExiste) throw new FormaDePagamentoInvalidaException(formaPagamento);
        return FormaPagamento.valueOf(formaPagamento);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public BigDecimal calcularTaxa(BigDecimal valorPago, Set<Taxa> taxas){
        BigDecimal valorTaxa = BigDecimal.valueOf(0);
        for (Taxa taxa : taxas) {
            valorTaxa = valorTaxa.add(taxa
                    .getValorFixo().add(taxa
                            .getPercentual()
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                            .multiply(valorPago)
                    )
            );
        }
        return valorTaxa;
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public BigDecimal validarSaldo(Long numero, BigDecimal valorPago, BigDecimal valorTaxa) {
        Conta conta = contaService.procurarContaAtiva(numero);

        BigDecimal valorTotal = valorPago.add(valorTaxa);

        if(conta.getSaldo().compareTo(valorTotal) < 0)
            throw new SaldoInsuficienteException("pagamento");

        return valorTotal;
    }
}
