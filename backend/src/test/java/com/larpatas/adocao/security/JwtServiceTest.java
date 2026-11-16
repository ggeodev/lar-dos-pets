package com.larpatas.adocao.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.larpatas.adocao.model.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

/** Testes unitários do token de login (sem Spring, sem banco). */
class JwtServiceTest {

    private static final String SEGREDO = "x".repeat(40);

    private Usuario usuario(long id) {
        Usuario u = new Usuario();
        ReflectionTestUtils.setField(u, "id", id);
        return u;
    }

    @Test
    @DisplayName("token gerado é válido e devolve o id do usuário")
    void tokenGeradoEhValido() {
        JwtService jwt = new JwtService(SEGREDO, 1);
        assertThat(jwt.validar(jwt.gerar(usuario(42L)))).contains(42L);
    }

    @Test
    @DisplayName("token com a assinatura trocada é recusado")
    void tokenAdulteradoEhRecusado() {
        JwtService jwt = new JwtService(SEGREDO, 1);
        String[] partes = jwt.gerar(usuario(1L)).split("\\.");
        String adulterado = partes[0] + "." + partes[1] + "." + "A".repeat(partes[2].length());
        assertThat(jwt.validar(adulterado)).isEmpty();
    }

    @Test
    @DisplayName("token assinado com outro segredo é recusado")
    void tokenDeOutroSegredoEhRecusado() {
        String token = new JwtService("y".repeat(40), 1).gerar(usuario(1L));
        assertThat(new JwtService(SEGREDO, 1).validar(token)).isEmpty();
    }

    @Test
    @DisplayName("token expirado é recusado")
    void tokenExpiradoEhRecusado() {
        JwtService jwt = new JwtService(SEGREDO, -1);   // validade negativa: já nasce vencido
        assertThat(jwt.validar(jwt.gerar(usuario(1L)))).isEmpty();
    }

    @Test
    @DisplayName("texto qualquer não é um token válido")
    void lixoEhRecusado() {
        assertThat(new JwtService(SEGREDO, 1).validar("isso-nao-e-um-token")).isEmpty();
    }

    @Test
    @DisplayName("segredo curto demais impede a aplicação de iniciar")
    void segredoCurtoEhRejeitado() {
        assertThatThrownBy(() -> new JwtService("curto", 1)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("sem segredo configurado, um segredo aleatório é gerado")
    void semSegredoGeraAleatorio() {
        JwtService jwt = new JwtService("", 1);
        assertThat(jwt.validar(jwt.gerar(usuario(7L)))).contains(7L);
    }
}
