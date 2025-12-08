package com.senai.novo_conta_bancaria.application.service;

import com.senai.novo_conta_bancaria.application.dto.dispositivo_iot.DispositivoIoTRegistroDTO;
import com.senai.novo_conta_bancaria.application.dto.dispositivo_iot.DispositivoIoTResponseDTO;
import com.senai.novo_conta_bancaria.domain.entity.Cliente;
import com.senai.novo_conta_bancaria.domain.entity.DispositivoIoT;
import com.senai.novo_conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.novo_conta_bancaria.domain.repository.DispositivoIoTRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DispositivoIoTService {
    private final DispositivoIoTRepository repository;
    private final ClienteService clienteService;

    // CREATE: embutido em Cliente

    // READ
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public List<DispositivoIoTResponseDTO> listarTodosOsDispositivoIoTs() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(DispositivoIoTResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public DispositivoIoTResponseDTO buscarDispositivoIoT(String codigoSerial) {
        return DispositivoIoTResponseDTO.fromEntity(procurarDispositivoIoTAtivo(codigoSerial));
    }

    // UPDATE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public DispositivoIoTResponseDTO atualizarDispositivoIoT(String codigoSerial, DispositivoIoTRegistroDTO dto) {
        Cliente cliente = clienteService.procurarClienteAtivo(dto.clienteCPF());
        DispositivoIoT DispositivoIoT = procurarDispositivoIoTAtivo(codigoSerial);

        DispositivoIoT.setCodigoSerial(dto.codigoSerial());
        DispositivoIoT.setChavePublica(dto.chavePublica());
        DispositivoIoT.setCliente(cliente);

        return DispositivoIoTResponseDTO.fromEntity(repository.save(DispositivoIoT));
    }

    // DELETE: permitido apenas com a deleção de cliente

    // Mét0do auxiliar para as requisições
    protected DispositivoIoT procurarDispositivoIoTAtivo(String codigoSerial) {
        return repository
                .findByCodigoSerialAndAtivoTrue(codigoSerial)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("DispositivoIoT"));
    }
}