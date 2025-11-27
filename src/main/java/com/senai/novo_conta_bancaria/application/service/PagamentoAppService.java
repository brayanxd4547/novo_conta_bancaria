package com.senai.novo_conta_bancaria.application.service;

import com.senai.novo_conta_bancaria.application.dto.ContaResumoDto;
import com.senai.novo_conta_bancaria.application.dto.PagamentoRegistroDto;
import com.senai.novo_conta_bancaria.application.dto.PagamentoResponseDto;
import com.senai.novo_conta_bancaria.domain.entity.Conta;
import com.senai.novo_conta_bancaria.domain.entity.Pagamento;
import com.senai.novo_conta_bancaria.domain.repository.ContaRepository;
import com.senai.novo_conta_bancaria.domain.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class PagamentoAppService {
    private final PagamentoRepository pagamentoRepository;
    private final ContaRepository contaRepository;
    private final ContaService contaService;
    private final PagamentoDomainService pagamentoDomainService;

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDto pagar(Long numero, PagamentoRegistroDto dto) {
        Conta conta = contaService.procurarContaAtiva(numero);

        BigDecimal valorDasTaxas = pagamentoDomainService.calcularTaxas(dto.formaPagamento(), dto.valorServico());
        conta.setSaldo(conta.getSaldo().subtract(dto.valorServico().add(valorDasTaxas)));

        return ContaResumoDto.fromEntity(contaRepository.save(conta));

        Pagamento pagamento = PagamentoRegistroDto.toEntity();
        return PagamentoResponseDto.fromEntity(pagamentoRepository.save(pagamento));
    }
}