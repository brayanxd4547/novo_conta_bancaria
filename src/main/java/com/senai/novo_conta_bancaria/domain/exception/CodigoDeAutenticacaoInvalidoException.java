package com.senai.novo_conta_bancaria.domain.exception;

public class CodigoDeAutenticacaoInvalidoException extends RuntimeException {
    public CodigoDeAutenticacaoInvalidoException() {
        super("O código de autenticação retornado durante o processo de biometria é inválido.");
    }
}