package com.larpatas.adocao.dto;

import com.larpatas.adocao.model.*;

/**
 * Dados públicos do animal. Contato do responsável não aparece aqui de propósito:
 * quem tem interesse usa o formulário, e o responsável vê os pedidos no painel.
 */
public record AnimalResponse(
        Long id, String nome, Especie especie, Porte porte, Sexo sexo, String idade,
        String cidade, String descricao, String saude, String temperamento,
        String fotoUrl, String responsavelNome, boolean destaque, StatusAnimal status) {

    public static AnimalResponse de(Animal a) {
        return new AnimalResponse(a.getId(), a.getNome(), a.getEspecie(), a.getPorte(), a.getSexo(),
                a.getIdade(), a.getCidade(), a.getDescricao(), a.getSaude(), a.getTemperamento(),
                a.getFotoUrl(), a.getResponsavel().getNome(), a.isDestaque(), a.getStatus());
    }
}
