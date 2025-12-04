package com.senai.novo_conta_bancaria.application.service;

import com.senai.novo_conta_bancaria.application.dto.cliente.ClienteAtualizacaoDto;
import com.senai.novo_conta_bancaria.application.dto.cliente.ClienteRegistroDto;
import com.senai.novo_conta_bancaria.application.dto.cliente.ClienteResponseDto;
import com.senai.novo_conta_bancaria.domain.entity.Cliente;
import com.senai.novo_conta_bancaria.domain.entity.Conta;
import com.senai.novo_conta_bancaria.domain.exception.ContaDeMesmoTipoException;
import com.senai.novo_conta_bancaria.domain.exception.EmailJaCadastradoException;
import com.senai.novo_conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.novo_conta_bancaria.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {
    private final ClienteRepository repository;
    private final PasswordEncoder passwordEncoder;

    // CREATE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public ClienteResponseDto registrarCliente(ClienteRegistroDto dto) {
        // Impede que mais de um cliente com mesmo email exista
        if (repository.existsByEmailAndAtivoTrue(dto.email())) {
            String mensagem = "Endereço de e-mail \"" + dto.email() + "\" já foi cadastrado.";
            throw new EmailJaCadastradoException(mensagem);
        }

        Cliente clienteRegistrado = repository
                .findByCpfAndAtivoTrue(dto.cpf()) // verifica se o cpf já está cadastrado
                .orElseGet( // se não estiver, cria um novo cliente
                        () -> repository.save(dto.toEntity())
                );
        List<Conta> contas = clienteRegistrado.getContas();
        Conta novaConta = dto.conta().toEntity(clienteRegistrado);

        // Verifica se o cliente já tem uma conta do mesmo tipo
        boolean temMesmoTipo = contas
                .stream()
                .anyMatch(c -> c.getTipo().equals(novaConta.getTipo()) && c.isAtivo());
        if (temMesmoTipo)
            throw new ContaDeMesmoTipoException(novaConta.getTipo());

        clienteRegistrado.getContas().add(novaConta);
        clienteRegistrado.setSenha(passwordEncoder.encode(dto.senha()));
        return ClienteResponseDto.fromEntity(repository.save(clienteRegistrado));
    }

    // READ
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public List<ClienteResponseDto> listarTodosOsClientes() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(ClienteResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponseDto buscarCliente(Long cpf) {
        return ClienteResponseDto.fromEntity(procurarClienteAtivo(cpf));
    }

    // UPDATE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public ClienteResponseDto atualizarCliente(Long cpf, ClienteAtualizacaoDto dto) {
        Cliente cliente = procurarClienteAtivo(cpf);

        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());

        return ClienteResponseDto.fromEntity(repository.save(cliente));
    }

    // DELETE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public void apagarCliente(Long cpf) {
        Cliente cliente = procurarClienteAtivo(cpf);

        cliente.setAtivo(false);
        cliente.getContas()
                .forEach(c -> c.setAtivo(false));

        repository.save(cliente);
    }

    // Mét0do auxiliar para as requisições
    private Cliente procurarClienteAtivo(Long cpf) {
        return repository
                .findByCpfAndAtivoTrue(cpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("cliente"));
    }
}