package com.larpatas.adocao.dto;

import com.larpatas.adocao.model.*;

/** Linha do painel do responsável. */
public record MeuAnimalResponse(Long id, String nome, Especie especie, String cidade,
                                StatusAnimal status, boolean destaque, long totalInteresses) {

    public static MeuAnimalResponse de(Animal a, long totalInteresses) {
        return new MeuAnimalResponse(a.getId(), a.getNome(), a.getEspecie(), a.getCidade(),
                a.getStatus(), a.isDestaque(), totalInteresses);
    }
}
