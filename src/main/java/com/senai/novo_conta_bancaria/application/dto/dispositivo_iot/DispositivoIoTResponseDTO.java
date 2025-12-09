package com.senai.novo_conta_bancaria.application.dto.dispositivo_iot;

import com.senai.novo_conta_bancaria.domain.entity.DispositivoIoT;

public record DispositivoIoTResponseDTO(
        String id,
        String codigoSerial,
        String chavePublica,
        Long clienteCPF
) {
    public static DispositivoIoTResponseDTO fromEntity(DispositivoIoT dispositivoIoT) {
        return new DispositivoIoTResponseDTO(
                dispositivoIoT.getId(),
                dispositivoIoT.getCodigoSerial(),
                dispositivoIoT.getChavePublica(),
                dispositivoIoT.getCliente().getCpf()
        );
    }
}