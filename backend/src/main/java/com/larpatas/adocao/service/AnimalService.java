package com.larpatas.adocao.service;

import com.larpatas.adocao.dto.*;
import com.larpatas.adocao.exception.RecursoNaoEncontradoException;
import com.larpatas.adocao.model.*;
import com.larpatas.adocao.repository.*;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnimalService {

    private final AnimalRepository animais;

    public AnimalService(AnimalRepository animais) { this.animais = animais; }

    @Transactional(readOnly = true)
    public List<AnimalResponse> listar(Especie especie, Porte porte, Sexo sexo, String cidade, Boolean destaque) {
        return animais.findAll(AnimalSpecs.disponiveisComFiltros(especie, porte, sexo, cidade, destaque),
                        Sort.by(Sort.Direction.DESC, "criadoEm"))
                .stream().map(AnimalResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public AnimalResponse buscar(Long id) {
        return AnimalResponse.de(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<String> cidades() {
        return animais.findCidadesPorStatus(StatusAnimal.DISPONIVEL);
    }

    @Transactional
    public AnimalResponse cadastrar(AnimalRequest r) {
        Animal a = new Animal();
        a.setNome(r.nome().trim());
        a.setEspecie(r.especie());
        a.setPorte(r.porte());
        a.setSexo(r.sexo());
        a.setIdade(r.idade().trim());
        a.setCidade(r.cidade().trim());
        a.setDescricao(r.descricao().trim());
        a.setSaude(r.saude().trim());
        a.setTemperamento(r.temperamento());
        a.setResponsavelNome(r.responsavelNome().trim());
        a.setResponsavelEmail(r.responsavelEmail().trim());
        a.setResponsavelTelefone(r.responsavelTelefone().trim());
        // destaque e status NÃO vêm do cliente: novo animal nasce disponível e sem destaque
        return AnimalResponse.de(animais.save(a));
    }

    Animal buscarEntidade(Long id) {
        return animais.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("Animal " + id + " não encontrado."));
    }
}
