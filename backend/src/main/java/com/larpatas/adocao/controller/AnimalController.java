package com.larpatas.adocao.controller;

import com.larpatas.adocao.dto.*;
import com.larpatas.adocao.model.*;
import com.larpatas.adocao.service.AnimalService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Endpoints de animais. Base: /api/animais */
@RestController
@RequestMapping("/api/animais")
public class AnimalController {

    private final AnimalService service;

    public AnimalController(AnimalService service) { this.service = service; }

    /** Catálogo + filtros. Ex.: GET /api/animais?especie=GATO&porte=PEQUENO&cidade=Araraquara, SP */
    @GetMapping
    public List<AnimalResponse> listar(@RequestParam(required = false) Especie especie,
                                       @RequestParam(required = false) Porte porte,
                                       @RequestParam(required = false) Sexo sexo,
                                       @RequestParam(required = false) String cidade,
                                       @RequestParam(required = false) Boolean destaque) {
        return service.listar(especie, porte, sexo, cidade, destaque);
    }

    /** Opções do filtro de localização. */
    @GetMapping("/cidades")
    public List<String> cidades() { return service.cidades(); }

    /** Perfil de um animal. */
    @GetMapping("/{id}")
    public AnimalResponse buscar(@PathVariable Long id) { return service.buscar(id); }

    /** Cadastro de novo animal. */
    @PostMapping
    public ResponseEntity<AnimalResponse> cadastrar(@Valid @RequestBody AnimalRequest request) {
        AnimalResponse criado = service.cadastrar(request);
        return ResponseEntity.created(URI.create("/api/animais/" + criado.id())).body(criado);
    }
}
