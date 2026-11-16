package com.larpatas.adocao.config;

import com.larpatas.adocao.repository.UsuarioRepository;
import com.larpatas.adocao.security.JwtAuthFilter;
import com.larpatas.adocao.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Regras de acesso:
 *  - público: catálogo/perfil (GET /api/animais), enviar interesse, criar conta e entrar
 *  - exige login: cadastrar animal e tudo em /api/meus-animais
 * A API é "stateless": nada de sessão no servidor, só o token JWT enviado a cada chamada.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filtros(HttpSecurity http, JwtService jwt, UsuarioRepository usuarios) throws Exception {
        http
            .csrf(csrf -> csrf.disable())   // sem cookies de sessão, não há CSRF a proteger
            .cors(Customizer.withDefaults()) // usa o bean corsConfigurationSource (CorsConfig)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a -> a
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/animais", "/api/animais/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/interesses").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/registro", "/api/auth/login").permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated())
            .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
            .addFilterBefore(new JwtAuthFilter(jwt, usuarios), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    /** Evita o usuário/senha automático do Spring Boot: o login é feito pelo AuthService. */
    @Bean
    UserDetailsService semUsuariosEmMemoria() {
        return username -> { throw new UsernameNotFoundException(username); };
    }
}
