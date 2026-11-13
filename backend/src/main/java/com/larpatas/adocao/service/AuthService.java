package com.larpatas.adocao.service;

import com.larpatas.adocao.dto.*;
import com.larpatas.adocao.exception.CredenciaisInvalidasException;
import com.larpatas.adocao.exception.RegraDeNegocioException;
import com.larpatas.adocao.model.Usuario;
import com.larpatas.adocao.repository.UsuarioRepository;
import com.larpatas.adocao.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UsuarioRepository usuarios, PasswordEncoder encoder, JwtService jwt) {
        this.usuarios = usuarios;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @Transactional
    public AuthResponse registrar(RegistroRequest r) {
        String email = r.email().trim().toLowerCase();
        if (usuarios.existsByEmail(email)) {
            throw new RegraDeNegocioException("Já existe uma conta com este e-mail.");
        }
        Usuario u = new Usuario();
        u.setNome(r.nome().trim());
        u.setEmail(email);
        u.setTelefone(r.telefone().trim());
        u.setSenhaHash(encoder.encode(r.senha()));
        return resposta(usuarios.save(u));
    }

    @Transactional(readOnly = true)
    public AuthResponse entrar(LoginRequest r) {
        Usuario u = usuarios.findByEmail(r.email().trim().toLowerCase())
                .filter(x -> encoder.matches(r.senha(), x.getSenhaHash()))
                .orElseThrow(CredenciaisInvalidasException::new);
        return resposta(u);
    }

    private AuthResponse resposta(Usuario u) {
        return new AuthResponse(jwt.gerar(u), new UsuarioResponse(u.getId(), u.getNome(), u.getEmail()));
    }
}
