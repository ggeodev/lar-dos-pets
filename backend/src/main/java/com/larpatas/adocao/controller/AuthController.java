package com.larpatas.adocao.controller;

import com.larpatas.adocao.dto.*;
import com.larpatas.adocao.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** Criar conta e entrar. Base: /api/auth */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) { this.service = service; }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse registrar(@Valid @RequestBody RegistroRequest request) {
        return service.registrar(request);
    }

    @PostMapping("/login")
    public AuthResponse entrar(@Valid @RequestBody LoginRequest request) {
        return service.entrar(request);
    }
}
