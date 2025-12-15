package com.senai.novo_conta_bancaria.domain.exception;

public class PagamentoNaoEncontradoException extends RuntimeException {
    public PagamentoNaoEncontradoException() {
        super("Não foi encontrado pagamento solicitado");
    }
}