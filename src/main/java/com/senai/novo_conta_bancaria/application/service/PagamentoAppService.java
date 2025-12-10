package com.senai.novo_conta_bancaria.application.service;

import com.rafaelcosta.spring_mqttx.domain.annotation.MqttPayload;
import com.rafaelcosta.spring_mqttx.domain.annotation.MqttSubscriber;
import com.senai.novo_conta_bancaria.application.dto.dispositivo_iot.ValidacaoPayloadDTO;
import com.senai.novo_conta_bancaria.application.dto.pagamento.PagamentoRegistroDto;
import com.senai.novo_conta_bancaria.application.dto.pagamento.PagamentoResponseDto;
import com.senai.novo_conta_bancaria.domain.entity.Cliente;
import com.senai.novo_conta_bancaria.domain.entity.Conta;
import com.senai.novo_conta_bancaria.domain.entity.Pagamento;
import com.senai.novo_conta_bancaria.domain.entity.Taxa;
import com.senai.novo_conta_bancaria.domain.enums.FormaPagamento;
import com.senai.novo_conta_bancaria.domain.exception.BiometriaInvalidadaException;
import com.senai.novo_conta_bancaria.domain.exception.CodigoDeAutenticacaoInvalidoException;
import com.senai.novo_conta_bancaria.domain.exception.SolicitacaoInterrompidaException;
import com.senai.novo_conta_bancaria.domain.repository.PagamentoRepository;
import com.senai.novo_conta_bancaria.infrastructure.mqtt.MqttPublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Service
@RequiredArgsConstructor
@Transactional
public class PagamentoAppService {
    private final PagamentoRepository pagamentoRepository;

    private final TaxaService taxaService;
    private final ContaService contaService;
    private final PagamentoDomainService pagamentoDomainService;

    private final MqttPublisherService mqtt;

    public CountDownLatch esperaBiometria;
    public CountDownLatch esperaValidacao;

    public String biometria;
    public String codigoAutenticacao;

    @Transactional
    @PreAuthorize("hasRole('CLIENTE')")
    public PagamentoResponseDto solicitarPagamento(Long numero, PagamentoRegistroDto dto) {
        esperaBiometria = new CountDownLatch(1);
        esperaValidacao = new CountDownLatch(1);

        // Espera o usuário realizar a biometria
        mqtt.solicitarBiometria();
        try {
            esperaBiometria.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SolicitacaoInterrompidaException("biometria");
        }

        Conta conta = contaService.procurarContaAtiva(numero);
        Cliente cliente = conta.getCliente();

        // Espera o IoT confirmar a validação
        codigoAutenticacao = UUID.randomUUID().toString();
        mqtt.solicitarAutenticacao(codigoAutenticacao, cliente.getId(), biometria);
        try {
            esperaValidacao.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SolicitacaoInterrompidaException("validação da biometria");
        }

        FormaPagamento formaPagamento = pagamentoDomainService.validarFormaPagamento(dto.formaPagamento());
        Set<Taxa> taxas = taxaService.procurarTaxasPorFormaPagamento(formaPagamento);

        BigDecimal valorTaxa = pagamentoDomainService.calcularTaxa(dto.valorPago(), taxas);
        BigDecimal valorTotal = pagamentoDomainService.validarSaldo(numero, dto.valorPago(), valorTaxa);
        conta.setSaldo(conta.getSaldo().subtract(valorTotal));

        Pagamento pagamento = dto.toEntity(conta, taxas);
        return PagamentoResponseDto.fromEntity(pagamentoRepository.save(pagamento), valorTaxa);
    }

    @MqttSubscriber("banco/salvarBiometria")
    public void salvarBiometria(@MqttPayload String biometria) {
        this.biometria = biometria;
        esperaBiometria.countDown();
    }

    @MqttSubscriber("banco/validacao")
    public void validarPagamento(@MqttPayload ValidacaoPayloadDTO dto) {
        try{
            if (!codigoAutenticacao.equals(dto.codigoAutenticacao()))
                throw new CodigoDeAutenticacaoInvalidoException();

            if (!dto.validado())
                throw new BiometriaInvalidadaException();
        } finally {
            esperaValidacao.countDown();
        }
    }
}