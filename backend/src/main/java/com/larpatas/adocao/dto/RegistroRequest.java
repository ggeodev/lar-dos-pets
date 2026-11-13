package com.larpatas.adocao.dto;

import jakarta.validation.constraints.*;

/** Corpo do POST /api/auth/registro. A senha tem no máximo 72 caracteres por limite do BCrypt. */
public record RegistroRequest(
        @NotBlank @Size(max = 100) String nome,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 30) String telefone,
        @NotBlank @Size(min = 8, max = 72) String senha) { }
