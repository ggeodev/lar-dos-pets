package com.larpatas.adocao.dto;

import com.larpatas.adocao.model.TipoMoradia;
import jakarta.validation.constraints.*;

/** Corpo do POST /api/interesses (formulário "Quero adotar"). */
public record InteresseRequest(
        @NotNull Long animalId,
        @NotBlank @Size(max = 120) String nome,
        @NotBlank @Size(max = 100) String cidade,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 30) String telefone,
        @NotNull TipoMoradia moradia,
        @NotNull Boolean possuiOutrosAnimais,
        @NotBlank @Size(max = 2000) String motivo,
        @AssertTrue(message = "É preciso aceitar o termo de responsabilidade") boolean aceitouTermo) { }
