package com.larpatas.adocao.exception;

/** Gera resposta 409 (pedido válido, mas não permitido no estado atual). */
public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String mensagem) { super(mensagem); }
}
