package com.larpatas.adocao.dto;

import com.larpatas.adocao.model.Interesse;
import java.time.Instant;

public record InteresseResponse(Long id, Long animalId, Instant criadoEm) {
    public static InteresseResponse de(Interesse i) {
        return new InteresseResponse(i.getId(), i.getAnimal().getId(), i.getCriadoEm());
    }
}
