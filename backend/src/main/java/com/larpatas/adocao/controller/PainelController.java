package com.larpatas.adocao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.larpatas.adocao.dto.*;
import com.larpatas.adocao.security.UsuarioLogado;
import com.larpatas.adocao.service.PainelService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/** Painel do responsável (tudo exige login). Base: /api/meus-animais */
@Tag(name = "Painel do responsável")
@RestController
@RequestMapping("/api/meus-animais")
public class PainelController {

    private final PainelService service;

    public PainelController(PainelService service) { this.service = service; }

    /** Animais da conta logada, com o total de interessados. */
    @Operation(summary = "Meus animais, com total de interessados")
    @GetMapping
    public List<MeuAnimalResponse> listar(@AuthenticationPrincipal UsuarioLogado usuario) {
        return service.listar(usuario.id());
    }

    /** Pedidos de adoção de um animal (com contato dos interessados). */
    @Operation(summary = "Pedidos de adoção de um dos meus animais (com contato)")
    @GetMapping("/{id}/interesses")
    public List<InteresseDetalheResponse> interessados(@PathVariable Long id,
                                                       @AuthenticationPrincipal UsuarioLogado usuario) {
        return service.interessados(usuario.id(), id);
    }

    /** Marcar como ADOTADO ou reabrir (DISPONIVEL). */
    @Operation(summary = "Marcar como ADOTADO ou reabrir (DISPONIVEL)")
    @PatchMapping("/{id}/status")
    public MeuAnimalResponse alterarStatus(@PathVariable Long id,
                                           @Valid @RequestBody StatusRequest request,
                                           @AuthenticationPrincipal UsuarioLogado usuario) {
        return service.alterarStatus(usuario.id(), id, request.status());
    }
}
