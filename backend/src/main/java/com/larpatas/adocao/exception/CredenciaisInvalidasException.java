package com.larpatas.adocao.exception;

/** Gera resposta 401. A mensagem é a mesma para e-mail inexistente e senha errada, de propósito. */
public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException() { super("E-mail ou senha incorretos."); }
}
