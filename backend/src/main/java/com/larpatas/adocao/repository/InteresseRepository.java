package com.larpatas.adocao.repository;

import com.larpatas.adocao.model.Interesse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InteresseRepository extends JpaRepository<Interesse, Long> {

    List<Interesse> findByAnimalIdOrderByCriadoEmDesc(Long animalId);

    long countByAnimalId(Long animalId);

    /** Evita o mesmo e-mail pedir o mesmo animal várias vezes. */
    boolean existsByAnimalIdAndEmailIgnoreCase(Long animalId, String email);

    /** Linhas [animalId, total] dos animais de um responsável. */
    @Query("select i.animal.id, count(i) from Interesse i where i.animal.responsavel.id = :uid group by i.animal.id")
    List<Object[]> contarPorAnimalDoResponsavel(@Param("uid") Long uid);
}
