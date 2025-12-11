package com.senai.novo_conta_bancaria.application.service;

import com.senai.novo_conta_bancaria.application.dto.gerente.GerenteAtualizacaoDto;
import com.senai.novo_conta_bancaria.application.dto.gerente.GerenteRegistroDto;
import com.senai.novo_conta_bancaria.application.dto.gerente.GerenteResponseDto;
import com.senai.novo_conta_bancaria.domain.entity.Gerente;
import com.senai.novo_conta_bancaria.domain.exception.EmailJaCadastradoException;
import com.senai.novo_conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.novo_conta_bancaria.domain.repository.GerenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GerenteService {
    private final GerenteRepository repository;
    private final PasswordEncoder passwordEncoder;

    // CREATE
    @PreAuthorize("hasRole('ADMIN')")
    public GerenteResponseDto registrarGerente(GerenteRegistroDto dto) {
        validarEmail(dto.email());

        Gerente gerenteRegistrado = repository.findByCpf(dto.cpf())
                .map(g -> g.isAtivo() ?
                        g :
                        reativarGerente(g, dto)
                )
                .orElseGet(() -> {
                            Gerente gerente = dto.toEntity();
                            gerente.setSenha(passwordEncoder.encode(dto.senha()));
                            return gerente;
                        }
                );

        return GerenteResponseDto.fromEntity(repository.save(gerenteRegistrado));
    }

    // READ
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public List<GerenteResponseDto> listarTodosOsGerentes() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(GerenteResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public GerenteResponseDto buscarGerente(Long cpf) {
        return GerenteResponseDto.fromEntity(procurarGerenteAtivo(cpf));
    }

    // UPDATE
    @PreAuthorize("hasRole('ADMIN')")
    public GerenteResponseDto atualizarGerente(Long cpf, GerenteAtualizacaoDto dto) {
        Gerente gerente = procurarGerenteAtivo(cpf);

        gerente.setNome(dto.nome());
        gerente.setCpf(dto.cpf());
        gerente.setEmail(dto.email());
        gerente.setSenha(dto.senha());

        return GerenteResponseDto.fromEntity(repository.save(gerente));
    }

    // DELETE
    @PreAuthorize("hasRole('ADMIN')")
    public void apagarGerente(Long cpf) {
        Gerente gerente = procurarGerenteAtivo(cpf);

        gerente.setAtivo(false);

        repository.save(gerente);
    }

    // Mét0do auxiliar para as requisições
    private Gerente procurarGerenteAtivo(Long cpf) {
        return repository
                .findByCpfAndAtivoTrue(cpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("gerente"));
    }

    private void validarEmail(String email) {
        if (repository.existsByEmailAndAtivoTrue(email))
            throw new EmailJaCadastradoException("Endereço de e-mail \"" + email + "\" já foi cadastrado.");
    }

    private Gerente reativarGerente(Gerente gerente, GerenteRegistroDto dto) {
        gerente.setAtivo(true);
        gerente.setNome(dto.nome());
        gerente.setEmail(dto.email());
        gerente.setSenha(passwordEncoder.encode(dto.senha()));

        return gerente;
    }
}