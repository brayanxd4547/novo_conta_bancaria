package com.senai.novo_conta_bancaria.application.service;

import com.senai.novo_conta_bancaria.application.dto.cliente.ClienteAtualizacaoDto;
import com.senai.novo_conta_bancaria.application.dto.cliente.ClienteRegistroDto;
import com.senai.novo_conta_bancaria.application.dto.cliente.ClienteResponseDto;
import com.senai.novo_conta_bancaria.domain.entity.Cliente;
import com.senai.novo_conta_bancaria.domain.entity.Conta;
import com.senai.novo_conta_bancaria.domain.entity.DispositivoIoT;
import com.senai.novo_conta_bancaria.domain.exception.ContaDeMesmoTipoException;
import com.senai.novo_conta_bancaria.domain.exception.EmailJaCadastradoException;
import com.senai.novo_conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.novo_conta_bancaria.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {
    private final ClienteRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ContaService contaService;

    // CREATE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public ClienteResponseDto registrarCliente(ClienteRegistroDto dto) {
        validarEmail(dto.email());

        Cliente clienteRegistrado = repository.findByCpf(dto.cpf())
                .map(c -> c.isAtivo() ?
                        inserirNovaConta(c, dto.conta().toEntity(c)) :
                        reativarCliente(c, dto)
                )
                .orElseGet(() -> criarNovoCliente(dto));

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
        cliente.getDispositivoIoT().setAtivo(false);

        repository.save(cliente);
    }

    // Mét0do auxiliar para as requisições
    protected Cliente procurarClienteAtivo(Long cpf) {
        return repository
                .findByCpfAndAtivoTrue(cpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("cliente"));
    }

    protected Cliente procurarClienteAtivoPorId(String id) {
        return repository
                .findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("cliente"));
    }

    private void validarEmail(String email) {
        if (repository.existsByEmailAndAtivoTrue(email))
            throw new EmailJaCadastradoException("Endereço de e-mail \"" + email + "\" já foi cadastrado.");
    }

    private Cliente inserirNovaConta(Cliente cliente, Conta conta) {
        List<Conta> contas = cliente.getContas();
        boolean temMesmoTipo = contas
                .stream()
                .anyMatch(c -> c.getTipo().equals(conta.getTipo()) && c.isAtivo());
        if (temMesmoTipo)
            throw new ContaDeMesmoTipoException(conta.getTipo());

        cliente.getContas().add(conta);
        return cliente;
    }

    private Cliente reativarCliente(Cliente cliente, ClienteRegistroDto dto) {
        // Se encontrar conta de mesmo tipo, substitui. Se não, registra uma nova
        Conta conta = cliente.getContas().stream()
                .filter(c -> c.getTipo().equals(dto.conta().tipo()))
                .findFirst()
                .map(c -> contaService.substituirConta(c, dto.conta()))
                .orElse(dto.conta().toEntity(cliente));
        DispositivoIoT dispositivo = dto.dispositivoIoT().toEntity(cliente);

        cliente.setAtivo(true);
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setSenha(passwordEncoder.encode(dto.senha()));
        cliente.setContas(new ArrayList<>(List.of(conta))); // Sobrescreve com uma nova lista de contas
        cliente.setDispositivoIoT(dispositivo);
        cliente.setCodigosAutenticacao(new ArrayList<>()); // Não incluído por enquanto

        return cliente;
    }

    private Cliente criarNovoCliente(ClienteRegistroDto dto) {
        Cliente cliente = dto.toEntity();
        Conta conta = dto.conta().toEntity(cliente);
        DispositivoIoT dispositivo = dto.dispositivoIoT().toEntity(cliente);

        cliente.setContas(List.of(conta));
        cliente.setDispositivoIoT(dispositivo);
        cliente.setSenha(passwordEncoder.encode(dto.senha()));

        return cliente;
    }
}