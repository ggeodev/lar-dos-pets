package com.larpatas.adocao.dto;

/** Resposta de login/registro: o token deve ser enviado em "Authorization: Bearer ...". */
public record AuthResponse(String token, UsuarioResponse usuario) { }
