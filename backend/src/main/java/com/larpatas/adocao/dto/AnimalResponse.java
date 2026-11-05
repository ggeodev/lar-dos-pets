package com.larpatas.adocao.dto;

import com.larpatas.adocao.model.*;

/**
 * Dados públicos do animal. Repare que e-mail e telefone do responsável
 * NÃO aparecem aqui de propósito: o contato passa pelo formulário de interesse.
 */
public record AnimalResponse(
        Long id, String nome, Especie especie, Porte porte, Sexo sexo, String idade,
        String cidade, String descricao, String saude, String temperamento,
        String fotoUrl, String responsavelNome, boolean destaque, StatusAnimal status) {

    public static AnimalResponse de(Animal a) {
        return new AnimalResponse(a.getId(), a.getNome(), a.getEspecie(), a.getPorte(), a.getSexo(),
                a.getIdade(), a.getCidade(), a.getDescricao(), a.getSaude(), a.getTemperamento(),
                a.getFotoUrl(), a.getResponsavelNome(), a.isDestaque(), a.getStatus());
    }
}
