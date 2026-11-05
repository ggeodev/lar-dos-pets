package com.larpatas.adocao.controller;

import com.larpatas.adocao.dto.*;
import com.larpatas.adocao.service.InteresseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** Registro de interesse em adotar. Base: /api/interesses */
@RestController
@RequestMapping("/api/interesses")
public class InteresseController {

    private final InteresseService service;

    public InteresseController(InteresseService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InteresseResponse registrar(@Valid @RequestBody InteresseRequest request) {
        return service.registrar(request);
    }
}
