package com.larpatas.adocao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Permite que o frontend (GitHub Pages, arquivo local ou outra porta) chame a API.
 * As origens vêm de app.cors.origens (variável CORS_ORIGINS). O padrão "*" libera
 * qualquer origem: use só em desenvolvimento.
 */
@Configuration
public class CorsConfig {
    @Bean
    WebMvcConfigurer corsConfigurer(@Value("${app.cors.origens}") String[] origens) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOriginPatterns(origens)
                        .allowedMethods("GET", "POST", "OPTIONS");
            }
        };
    }
}
