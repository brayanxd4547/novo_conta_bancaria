package com.senai.novo_conta_bancaria.domain.exception;

public class SolicitacaoInterrompidaException extends RuntimeException {
    public SolicitacaoInterrompidaException(String solicitacao) {
        super("O aguardo pela solicitação de " + solicitacao + " foi interrompido.");
    }
}