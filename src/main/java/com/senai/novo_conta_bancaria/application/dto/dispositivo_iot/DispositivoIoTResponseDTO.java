package com.senai.novo_conta_bancaria.application.dto.dispositivo_iot;

import com.senai.novo_conta_bancaria.domain.entity.Cliente;
import com.senai.novo_conta_bancaria.domain.entity.DispositivoIoT;
import com.senai.novo_conta_bancaria.domain.entity.Taxa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DispositivoIoTResponseDTO(
        String id,

        String codigoSerial,

        String chavePublica,

        Cliente cliente
) {
    public static DispositivoIoTResponseDTO fromEntity(DispositivoIoT dispositivoIoT) {
        return new DispositivoIoTResponseDTO(
                dispositivoIoT.getId(),
                dispositivoIoT.getCodigoSerial(),
                dispositivoIoT.getChavePublica(),
                dispositivoIoT.getCliente()
        );
    }
}