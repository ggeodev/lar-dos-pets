package com.larpatas.adocao.security;

import com.larpatas.adocao.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** Cria e valida os tokens de login. O token guarda apenas o id do usuário. */
@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    private final SecretKey chave;
    private final long validadeMs;

    public JwtService(@Value("${app.jwt.segredo:}") String segredo,
                      @Value("${app.jwt.validade-horas:12}") long validadeHoras) {
        if (segredo == null || segredo.isBlank()) {
            byte[] aleatorio = new byte[48];
            new SecureRandom().nextBytes(aleatorio);
            segredo = Base64.getEncoder().encodeToString(aleatorio);
            log.warn("JWT_SECRET não definido: usando segredo aleatório. Os logins serão invalidados a cada reinício.");
        } else if (segredo.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("JWT_SECRET precisa ter pelo menos 32 caracteres.");
        }
        this.chave = Keys.hmacShaKeyFor(segredo.getBytes(StandardCharsets.UTF_8));
        this.validadeMs = validadeHoras * 3_600_000L;
    }

    public String gerar(Usuario u) {
        Date agora = new Date();
        return Jwts.builder()
                .subject(String.valueOf(u.getId()))
                .issuedAt(agora)
                .expiration(new Date(agora.getTime() + validadeMs))
                .signWith(chave)
                .compact();
    }

    /** Devolve o id do usuário se o token for válido e não estiver expirado. */
    public Optional<Long> validar(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(chave).build().parseSignedClaims(token).getPayload();
            return Optional.of(Long.valueOf(claims.getSubject()));
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
