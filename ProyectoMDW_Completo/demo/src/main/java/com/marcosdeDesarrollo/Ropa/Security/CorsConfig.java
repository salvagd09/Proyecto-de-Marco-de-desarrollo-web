package com.marcosdeDesarrollo.Ropa.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Permitir credenciales (importante para cookies, auth headers, etc.)
        config.setAllowCredentials(true);

        // Permitir peticiones desde cualquier origen. Para producción, deberías restringirlo.
        // Ejemplo: config.setAllowedOrigins(Arrays.asList("http://tusitio.com"));
        config.setAllowedOrigins(List.of("*"));

        // Permitir todas las cabeceras (incluyendo 'Authorization')
        config.setAllowedHeaders(List.of("*"));

        // Permitir todos los métodos HTTP (GET, POST, PUT, DELETE, OPTIONS, etc.)
        config.setAllowedMethods(List.of("*"));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}