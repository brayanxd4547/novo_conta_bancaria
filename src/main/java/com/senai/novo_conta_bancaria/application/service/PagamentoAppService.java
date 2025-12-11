package com.senai.novo_conta_bancaria.application.service;

import com.rafaelcosta.spring_mqttx.domain.annotation.MqttPayload;
import com.rafaelcosta.spring_mqttx.domain.annotation.MqttSubscriber;
import com.senai.novo_conta_bancaria.application.dto.dispositivo_iot.ValidacaoPayloadDTO;
import com.senai.novo_conta_bancaria.application.dto.pagamento.PagamentoRegistroDto;
import com.senai.novo_conta_bancaria.application.dto.pagamento.PagamentoResponseDto;
import com.senai.novo_conta_bancaria.domain.entity.*;
import com.senai.novo_conta_bancaria.domain.enums.FormaPagamento;
import com.senai.novo_conta_bancaria.domain.enums.StatusPagamento;
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
    private final PagamentoRepository repository;

    private final TaxaService taxaService;
    private final ContaService contaService;
    private final PagamentoDomainService pagamentoDomainService;

    private final MqttPublisherService mqtt;

    @Transactional
    @PreAuthorize("hasRole('CLIENTE')")
    public PagamentoResponseDto solicitarPagamento(Long numero, PagamentoRegistroDto dto){
        String codigoAutenticacao = UUID.randomUUID().toString();

        Conta conta = contaService.procurarContaAtiva(numero);

        FormaPagamento formaPagamento = pagamentoDomainService.validarFormaPagamento(dto.formaPagamento());
        Set<Taxa> taxas = taxaService.procurarTaxasPorFormaPagamento(formaPagamento);
        BigDecimal valorTaxa = pagamentoDomainService.calcularTaxa(dto.valorPago(), taxas);

        Pagamento pagamento = dto.toEntity(conta, taxas);

        mqtt.solicitarBiometria();
        return PagamentoResponseDto.fromEntity(repository.save(pagamento), valorTaxa);
    }

    /*@Transactional
    @PreAuthorize("hasRole('CLIENTE')")
    public PagamentoResponseDto ddd(Long numero, PagamentoRegistroDto dto) {
        // Espera o usuário realizar a biometria
        mqtt.solicitarBiometria();

        String biometria = salvarBiometria();

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




        conta.setSaldo(conta.getSaldo().subtract(valorTotal));

        Pagamento pagamento = dto.toEntity(conta, taxas);
        return PagamentoResponseDto.fromEntity(pagamentoRepository.save(pagamento), valorTaxa);
    }
     */

    @MqttSubscriber("banco/receberBiometria")
    public String receberBiometria(@MqttPayload String tokenBiometria) {


        Pagamento pagamento = repository.findBy()

        mqtt.solicitarAutenticacao(codigoAutenticacao, .getId(), tokenBiometria);

        return null;
    }

    @MqttSubscriber("banco/validacao")
    public void validarPagamento(@MqttPayload ValidacaoPayloadDTO dto) {
        if (!codigoAutenticacao.equals(dto.codigoAutenticacao()))
            throw new CodigoDeAutenticacaoInvalidoException();

        if (!dto.validado())
            throw new BiometriaInvalidadaException();


    }
}