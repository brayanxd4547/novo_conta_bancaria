package com.senai.novo_conta_bancaria.application.dto.dispositivo_iot;

public record BiometriaPayloadDTO(
        String idPagamento,
        String biometria
){
    public static BiometriaPayloadDTO toDto(String codigoAutenticacao, String idPagamento, String biometria){
        return new BiometriaPayloadDTO(
                idPagamento,
                biometria
        );
    }
}