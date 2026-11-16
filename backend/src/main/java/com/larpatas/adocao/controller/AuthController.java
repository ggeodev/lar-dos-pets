package com.larpatas.adocao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.larpatas.adocao.dto.*;
import com.larpatas.adocao.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** Criar conta e entrar. Base: /api/auth */
@Tag(name = "Conta")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) { this.service = service; }

    @Operation(summary = "Criar conta de responsável (já devolve o token de login)")
    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse registrar(@Valid @RequestBody RegistroRequest request) {
        return service.registrar(request);
    }

    @Operation(summary = "Entrar e receber o token")
    @PostMapping("/login")
    public AuthResponse entrar(@Valid @RequestBody LoginRequest request) {
        return service.entrar(request);
    }
}
