package com.senai.novo_conta_bancaria.application.service;

import com.senai.novo_conta_bancaria.application.dto.conta.ContaAtualizacaoDto;
import com.senai.novo_conta_bancaria.application.dto.conta.ContaResumoDto;
import com.senai.novo_conta_bancaria.application.dto.conta.TransferenciaDto;
import com.senai.novo_conta_bancaria.application.dto.conta.ValorSaqueDepositoDto;
import com.senai.novo_conta_bancaria.domain.entity.Conta;
import com.senai.novo_conta_bancaria.domain.entity.ContaCorrente;
import com.senai.novo_conta_bancaria.domain.entity.ContaPoupanca;
import com.senai.novo_conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.novo_conta_bancaria.domain.exception.RendimentoInvalidoException;
import com.senai.novo_conta_bancaria.domain.exception.TipoDeContaInvalidaException;
import com.senai.novo_conta_bancaria.domain.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContaService {
    private final ContaRepository repository;
    private final DispositivoIoTService dispositivoIoTService;

    // CREATE: embutido em Cliente

    // READ
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('CLIENTE')")
    public List<ContaResumoDto> listarTodasAsContas() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(ContaResumoDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('CLIENTE')")
    public List<ContaResumoDto> listarContasPorCpf(Long cpf) {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .filter(c -> c.getCliente().getCpf().equals(cpf))
                .map(ContaResumoDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDto buscarConta(Long numero) {
        return ContaResumoDto.fromEntity(procurarContaAtiva(numero));
    }

    // UPDATE
    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDto atualizarConta(Long numero, ContaAtualizacaoDto dto) {
        Conta conta = procurarContaAtiva(numero);

        conta.setSaldo(dto.saldo());
        if (conta instanceof ContaCorrente contaCorrente) {
            contaCorrente.setLimite(dto.limite());
            contaCorrente.setTaxa(dto.taxa());
        } else if (conta instanceof ContaPoupanca contaPoupanca) {
            contaPoupanca.setRendimento(dto.rendimento());
        } else {
            throw new TipoDeContaInvalidaException("");
        }

        return ContaResumoDto.fromEntity(repository.save(conta));
    }

    // DELETE
    @PreAuthorize("hasRole('CLIENTE')")
    public void apagarConta(Long numero) {
        Conta conta = procurarContaAtiva(numero);

        conta.setAtivo(false);

        repository.save(conta);
    }

    // Ações específicas

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDto sacar(Long numero, ValorSaqueDepositoDto dto) {
        Conta conta = procurarContaAtiva(numero);

        conta.sacar(dto.valor());

        return ContaResumoDto.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDto depositar(Long numero, ValorSaqueDepositoDto dto) {
        Conta conta = procurarContaAtiva(numero);

        conta.depositar(dto.valor());

        return ContaResumoDto.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDto transferir(Long numeroOrigem, TransferenciaDto dto) {
        Conta contaOrigem = procurarContaAtiva(numeroOrigem);
        Conta contaDestino = procurarContaAtiva(dto.numeroDestino());

        contaOrigem.transferir(contaDestino, dto.valor());

        repository.save(contaDestino);
        return ContaResumoDto.fromEntity(repository.save(contaOrigem));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDto rendimento(Long numero) {
        Conta conta = procurarContaAtiva(numero);

        if (!(conta instanceof ContaPoupanca contaPoupanca))
            throw new RendimentoInvalidoException();

        contaPoupanca.aplicarRendimento();

        return ContaResumoDto.fromEntity(repository.save(conta));
    }

    // Mét0do auxiliar para as requisições
    protected Conta procurarContaAtiva(Long numero) {
        return repository
                .findByNumeroAndAtivoTrue(numero)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("conta"));
    }

    protected Conta substituirConta(Conta contaAntiga, ContaResumoDto contaNova){
        contaAntiga.setAtivo(true);
        contaAntiga.setSaldo(contaNova.saldo());
        contaAntiga.setNumero(contaNova.numero());

        return contaAntiga;
    }
}