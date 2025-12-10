package com.senai.novo_conta_bancaria.application.dto.dispositivo_iot;

public record AutenticacaoPayloadDTO(
        String codigoAutenticacao,
        String idCliente,
        String biometria
){
    public static AutenticacaoPayloadDTO toDto(String codigoAutenticacao, String idCliente, String biometria){
        return new AutenticacaoPayloadDTO(
                codigoAutenticacao,
                idCliente,
                biometria
        );
    }
}