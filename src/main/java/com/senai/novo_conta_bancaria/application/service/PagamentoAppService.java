package com.senai.novo_conta_bancaria.application.service;

import com.senai.novo_conta_bancaria.application.dto.pagamento.PagamentoRegistroDto;
import com.senai.novo_conta_bancaria.application.dto.pagamento.PagamentoResponseDto;
import com.senai.novo_conta_bancaria.domain.entity.Conta;
import com.senai.novo_conta_bancaria.domain.entity.Pagamento;
import com.senai.novo_conta_bancaria.domain.entity.Taxa;
import com.senai.novo_conta_bancaria.domain.enums.FormaPagamento;
import com.senai.novo_conta_bancaria.domain.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class PagamentoAppService {
    private final PagamentoRepository pagamentoRepository;

    private final TaxaService taxaService;
    private final ContaService contaService;
    private final PagamentoDomainService pagamentoDomainService;

    @PreAuthorize("hasRole('CLIENTE')")
    public PagamentoResponseDto pagar(Long numero, PagamentoRegistroDto dto) {
        FormaPagamento formaPagamento = pagamentoDomainService.validarFormaPagamento(dto.formaPagamento());

        Set<Taxa> taxas = taxaService.procurarTaxasPorFormaPagamento(formaPagamento);
        Conta conta = contaService.procurarContaAtiva(numero);

        Pagamento pagamento = dto.toEntity(conta, taxas);

        BigDecimal valorTaxa = pagamentoDomainService.calcularTaxa(dto.valorPago(), taxas);
        BigDecimal valorTotal = pagamentoDomainService.validarSaldo(numero, dto.valorPago(), valorTaxa);

        conta.setSaldo(conta.getSaldo().subtract(valorTotal));
        return PagamentoResponseDto.fromEntity(pagamentoRepository.save(pagamento), valorTaxa);
    }
}