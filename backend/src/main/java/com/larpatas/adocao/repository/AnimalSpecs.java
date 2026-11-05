package com.larpatas.adocao.repository;

import com.larpatas.adocao.model.*;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/** Monta a consulta do catálogo combinando apenas os filtros informados. */
public final class AnimalSpecs {

    private AnimalSpecs() { }

    public static Specification<Animal> disponiveisComFiltros(
            Especie especie, Porte porte, Sexo sexo, String cidade, Boolean destaque) {
        return (root, query, cb) -> {
            List<Predicate> filtros = new ArrayList<>();
            filtros.add(cb.equal(root.get("status"), StatusAnimal.DISPONIVEL)); // adotados não aparecem
            if (especie != null) filtros.add(cb.equal(root.get("especie"), especie));
            if (porte != null)   filtros.add(cb.equal(root.get("porte"), porte));
            if (sexo != null)    filtros.add(cb.equal(root.get("sexo"), sexo));
            if (cidade != null && !cidade.isBlank())
                filtros.add(cb.equal(cb.lower(root.get("cidade")), cidade.trim().toLowerCase()));
            if (destaque != null) filtros.add(cb.equal(root.get("destaque"), destaque));
            return cb.and(filtros.toArray(new Predicate[0]));
        };
    }
}
