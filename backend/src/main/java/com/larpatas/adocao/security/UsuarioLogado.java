package com.larpatas.adocao.security;

/** Quem está autenticado na requisição atual. Use com @AuthenticationPrincipal nos controllers. */
public record UsuarioLogado(Long id, String nome, String email) { }
