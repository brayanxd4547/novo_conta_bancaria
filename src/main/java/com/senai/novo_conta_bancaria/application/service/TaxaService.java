package com.senai.novo_conta_bancaria.application.service;

import com.senai.novo_conta_bancaria.application.dto.TaxaAtualizacaoDto;
import com.senai.novo_conta_bancaria.application.dto.TaxaRegistroDto;
import com.senai.novo_conta_bancaria.application.dto.TaxaResponseDto;
import com.senai.novo_conta_bancaria.domain.entity.Taxa;
import com.senai.novo_conta_bancaria.domain.entity.Conta;
import com.senai.novo_conta_bancaria.domain.exception.ContaDeMesmoTipoException;
import com.senai.novo_conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.novo_conta_bancaria.domain.repository.TaxaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaxaService {
    private final TaxaRepository repository;

    // CREATE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public TaxaResponseDto registrarTaxa(TaxaRegistroDto dto) {
        Taxa taxaRegistrada = repository // verifica se a taxa já existe
                .findByDescricaoAndAtivoTrue(dto.descricao())
                .orElseGet( // se não existir, cria um novo
                        () -> repository.save(dto.toEntity())
                );

        return TaxaResponseDto.fromEntity(repository.save(taxaRegistrada));
    }

    // READ
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public List<TaxaResponseDto> listarTodasAsTaxas() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(TaxaResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaxaResponseDto buscarTaxa(String id) {
        return TaxaResponseDto.fromEntity(procurarTaxaAtivo(id));
    }

    // UPDATE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public TaxaResponseDto atualizarTaxa(String id, TaxaAtualizacaoDto dto) {
        Taxa taxa = procurarTaxaAtivo(id);

        taxa.setPercentual(dto.percentual());
        taxa.setValorFixo(dto.valorFixo());

        return TaxaResponseDto.fromEntity(repository.save(taxa));
    }

    // DELETE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public void apagarTaxa(String id) {
        Taxa taxa = procurarTaxaAtivo(id);

        taxa.setAtivo(false);

        repository.save(taxa);
    }

    // Mét0do auxiliar para as requisições
    private Taxa procurarTaxaAtivo(String id) {
        return repository
                .findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("taxa"));
    }
}