package com.senai.novo_conta_bancaria.application.dto.cliente;

import com.senai.novo_conta_bancaria.application.dto.conta.ContaResumoDto;
import com.senai.novo_conta_bancaria.application.dto.dispositivo_iot.DispositivoIoTResponseDTO;
import com.senai.novo_conta_bancaria.domain.entity.Cliente;

import java.util.List;

public record ClienteResponseDto(
        String id,
        String nome,
        Long cpf,
        String email,
        String senha,
        String biometria,
        List<ContaResumoDto> contas,
        DispositivoIoTResponseDTO dispositivoIoT
) {
    public static ClienteResponseDto fromEntity(Cliente cliente) {
        List<ContaResumoDto> contas = cliente
                .getContas()
                .stream()
                .map(ContaResumoDto::fromEntity)
                .toList();
        return new ClienteResponseDto(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getSenha(),
                cliente.getBiometria(),
                contas,
                DispositivoIoTResponseDTO.fromEntity(cliente.getDispositivoIoT())
        );
    }
}