package com.larpatas.adocao.dto;

import com.larpatas.adocao.model.Interesse;
import com.larpatas.adocao.model.TipoMoradia;
import java.time.Instant;

/** Pedido completo, com contato. Só é devolvido ao responsável dono do animal. */
public record InteresseDetalheResponse(Long id, String nome, String cidade, String email, String telefone,
                                       TipoMoradia moradia, boolean possuiOutrosAnimais, String motivo,
                                       Instant criadoEm) {

    public static InteresseDetalheResponse de(Interesse i) {
        return new InteresseDetalheResponse(i.getId(), i.getNome(), i.getCidade(), i.getEmail(),
                i.getTelefone(), i.getMoradia(), i.isPossuiOutrosAnimais(), i.getMotivo(), i.getCriadoEm());
    }
}
