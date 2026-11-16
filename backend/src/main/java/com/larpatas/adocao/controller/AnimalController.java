package com.larpatas.adocao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.larpatas.adocao.dto.*;
import com.larpatas.adocao.model.*;
import com.larpatas.adocao.security.UsuarioLogado;
import com.larpatas.adocao.service.AnimalService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/** Endpoints de animais. Base: /api/animais (leitura pública; cadastro exige login). */
@Tag(name = "Animais")
@RestController
@RequestMapping("/api/animais")
public class AnimalController {

    private final AnimalService service;

    public AnimalController(AnimalService service) { this.service = service; }

    /** Catálogo + filtros. Ex.: GET /api/animais?especie=GATO&porte=PEQUENO&cidade=Araraquara, SP */
    @Operation(summary = "Catálogo de animais disponíveis, com filtros opcionais")
    @GetMapping
    public List<AnimalResponse> listar(@RequestParam(required = false) Especie especie,
                                       @RequestParam(required = false) Porte porte,
                                       @RequestParam(required = false) Sexo sexo,
                                       @RequestParam(required = false) String cidade,
                                       @RequestParam(required = false) Boolean destaque) {
        return service.listar(especie, porte, sexo, cidade, destaque);
    }

    /** Números da página inicial (animais disponíveis, adotados e cidades). */
    @Operation(summary = "Estatísticas públicas da plataforma")
    @GetMapping("/estatisticas")
    public EstatisticasResponse estatisticas() { return service.estatisticas(); }

    /** Opções do filtro de localização. */
    @Operation(summary = "Cidades com animais disponíveis (filtro de localização)")
    @GetMapping("/cidades")
    public List<String> cidades() { return service.cidades(); }

    /** Perfil de um animal. */
    @Operation(summary = "Perfil de um animal")
    @GetMapping("/{id}")
    public AnimalResponse buscar(@PathVariable Long id) { return service.buscar(id); }

    /** Cadastro de novo animal pela conta logada. */
    @Operation(summary = "Cadastrar animal na conta logada")
    @PostMapping
    public ResponseEntity<AnimalResponse> cadastrar(@Valid @RequestBody AnimalRequest request,
                                                    @AuthenticationPrincipal UsuarioLogado usuario) {
        AnimalResponse criado = service.cadastrar(request, usuario.id());
        return ResponseEntity.created(URI.create("/api/animais/" + criado.id())).body(criado);
    }
}
