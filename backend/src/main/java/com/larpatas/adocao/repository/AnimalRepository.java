package com.larpatas.adocao.repository;

import com.larpatas.adocao.model.Animal;
import com.larpatas.adocao.model.StatusAnimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimalRepository extends JpaRepository<Animal, Long>, JpaSpecificationExecutor<Animal> {

    /** Cidades com animais disponíveis (alimenta o filtro de localização). */
    @Query("select distinct a.cidade from Animal a where a.status = :status order by a.cidade")
    List<String> findCidadesPorStatus(@Param("status") StatusAnimal status);
}
