package com.larpatas.adocao.dto;

import com.larpatas.adocao.model.*;
import jakarta.validation.constraints.*;

/** Corpo do POST /api/animais (formulário "Cadastrar animal"). */
public record AnimalRequest(
        @NotBlank @Size(max = 80) String nome,
        @NotNull Especie especie,
        @NotNull Porte porte,
        @NotNull Sexo sexo,
        @NotBlank @Size(max = 40) String idade,
        @NotBlank @Size(max = 100) String cidade,
        @NotBlank @Size(max = 2000) String descricao,
        @NotBlank @Size(max = 200) String saude,
        @Size(max = 200) String temperamento,
        @NotBlank @Size(max = 100) String responsavelNome,
        @NotBlank @Email @Size(max = 150) String responsavelEmail,
        @NotBlank @Size(max = 30) String responsavelTelefone) { }
