package com.larpatas.adocao.service;

import com.larpatas.adocao.dto.*;
import com.larpatas.adocao.exception.RecursoNaoEncontradoException;
import com.larpatas.adocao.model.*;
import com.larpatas.adocao.repository.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Operações do painel do responsável. Todas filtram pelo dono: ninguém acessa animal de outra conta. */
@Service
public class PainelService {

    private final AnimalRepository animais;
    private final InteresseRepository interesses;

    public PainelService(AnimalRepository animais, InteresseRepository interesses) {
        this.animais = animais;
        this.interesses = interesses;
    }

    @Transactional(readOnly = true)
    public List<MeuAnimalResponse> listar(Long responsavelId) {
        Map<Long, Long> totais = new HashMap<>();
        for (Object[] linha : interesses.contarPorAnimalDoResponsavel(responsavelId)) {
            totais.put((Long) linha[0], (Long) linha[1]);
        }
        return animais.findByResponsavelIdOrderByCriadoEmDesc(responsavelId).stream()
                .map(a -> MeuAnimalResponse.de(a, totais.getOrDefault(a.getId(), 0L)))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InteresseDetalheResponse> interessados(Long responsavelId, Long animalId) {
        buscarDoResponsavel(responsavelId, animalId);
        return interesses.findByAnimalIdOrderByCriadoEmDesc(animalId).stream()
                .map(InteresseDetalheResponse::de).toList();
    }

    @Transactional
    public MeuAnimalResponse alterarStatus(Long responsavelId, Long animalId, StatusAnimal status) {
        Animal a = buscarDoResponsavel(responsavelId, animalId);
        a.setStatus(status); // entidade gerenciada: gravada ao fim da transação
        return MeuAnimalResponse.de(a, interesses.countByAnimalId(animalId));
    }

    /** Animal de outra conta responde 404 (igual a inexistente), para não revelar que ele existe. */
    private Animal buscarDoResponsavel(Long responsavelId, Long animalId) {
        return animais.findByIdAndResponsavelId(animalId, responsavelId).orElseThrow(
                () -> new RecursoNaoEncontradoException("Animal " + animalId + " não encontrado."));
    }
}
