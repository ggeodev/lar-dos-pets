package com.larpatas.adocao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.larpatas.adocao.dto.*;
import com.larpatas.adocao.service.InteresseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** Registro de interesse em adotar. Base: /api/interesses */
@Tag(name = "Interesses")
@RestController
@RequestMapping("/api/interesses")
public class InteresseController {

    private final InteresseService service;

    public InteresseController(InteresseService service) { this.service = service; }

    @Operation(summary = "Manifestar interesse em adotar um animal")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InteresseResponse registrar(@Valid @RequestBody InteresseRequest request) {
        return service.registrar(request);
    }
}
