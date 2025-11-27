package com.senai.novo_conta_bancaria.domain.exception;

public class ContaDeMesmoTipoException extends RuntimeException {
    public ContaDeMesmoTipoException(String tipo) {
        super("Cliente já possui uma conta " + tipo);
    }
}