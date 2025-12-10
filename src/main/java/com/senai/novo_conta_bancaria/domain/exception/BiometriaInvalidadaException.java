package com.senai.novo_conta_bancaria.domain.exception;

public class BiometriaInvalidadaException extends RuntimeException {
    public BiometriaInvalidadaException() {
        super("A biometria inserida não corresponde à biometria do cliente possuidor da conta.");
    }
}