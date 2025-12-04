package com.senai.novo_conta_bancaria.domain.exception;

public class EmailJaCadastradoException extends RuntimeException {
    public EmailJaCadastradoException(String mensagem) {
        super(mensagem);
    }
}