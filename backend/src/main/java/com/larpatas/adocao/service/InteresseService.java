package com.larpatas.adocao.service;

import com.larpatas.adocao.dto.*;
import com.larpatas.adocao.exception.*;
import com.larpatas.adocao.model.*;
import com.larpatas.adocao.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InteresseService {

    private final InteresseRepository interesses;
    private final AnimalRepository animais;

    public InteresseService(InteresseRepository interesses, AnimalRepository animais) {
        this.interesses = interesses;
        this.animais = animais;
    }

    @Transactional
    public InteresseResponse registrar(InteresseRequest r) {
        Animal animal = animais.findById(r.animalId()).orElseThrow(
                () -> new RecursoNaoEncontradoException("Animal " + r.animalId() + " não encontrado."));
        if (animal.getStatus() != StatusAnimal.DISPONIVEL) {
            throw new RegraDeNegocioException("Este animal não está mais disponível para adoção.");
        }
        String email = r.email().trim();
        if (interesses.existsByAnimalIdAndEmailIgnoreCase(animal.getId(), email)) {
            throw new RegraDeNegocioException("Você já manifestou interesse neste animal com este e-mail.");
        }
        Interesse i = new Interesse();
        i.setAnimal(animal);
        i.setNome(r.nome().trim());
        i.setCidade(r.cidade().trim());
        i.setEmail(email);
        i.setTelefone(r.telefone().trim());
        i.setMoradia(r.moradia());
        i.setPossuiOutrosAnimais(r.possuiOutrosAnimais());
        i.setMotivo(r.motivo().trim());
        return InteresseResponse.de(interesses.save(i));
    }
}
