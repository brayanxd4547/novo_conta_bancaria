package com.senai.novo_conta_bancaria.application.dto.dispositivo_iot;

public record ValidacaoPayloadDTO(
        String codigoAutenticacao,
        boolean validado
){
    public static ValidacaoPayloadDTO toDto(String codigoAutenticacao, boolean validado){
        return new ValidacaoPayloadDTO(
                codigoAutenticacao,
                validado
        );
    }
}