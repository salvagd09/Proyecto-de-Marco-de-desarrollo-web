package com.marcosdeDesarrollo.Ropa.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException; // <-- ¡IMPORTANTE AÑADIR ESTA!
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${marcos.app.jwtSecret}")
    private String jwtSecret;

    @Value("${marcos.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // --- SECCIÓN DE DEPURACIÓN 1: VERIFICAR CONFIGURACIÓN AL INICIAR ---
    @PostConstruct
    public void init() {
        logger.error("========================================================================");
        logger.error("INICIANDO VERIFICACIÓN DE JWTUTILS");
        if (jwtSecret == null || jwtSecret.isBlank()) {
            logger.error("!!! ERROR CRÍTICO: 'marcos.app.jwtSecret' NO ESTÁ DEFINIDO en application.properties !!!");
        } else {
            logger.info("OK: 'jwtSecret' cargado (longitud: {})", jwtSecret.length());
        }

        if (jwtExpirationMs <= 0) {
            logger.error("!!! ERROR CRÍTICO: 'jwtExpirationMs' es CERO o NEGATIVO ({}). Los tokens expiran al instante.", jwtExpirationMs);
        } else {
            logger.info("OK: 'jwtExpirationMs' cargado: {} ms ({} segundos)", jwtExpirationMs, jwtExpirationMs / 1000);
        }
        logger.error("========================================================================");
    }

    // --- SECCIÓN DE DEPURACIÓN 2: REGISTROS AL GENERAR EL TOKEN ---
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtExpirationMs);

        logger.info("--- GENERANDO TOKEN PARA '{}' ---", userPrincipal.getUsername());
        logger.info("Hora actual (IssuedAt): {}", now);
        logger.info("Hora de expiración: {}", expirationDate);

        String token = Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

        logger.info("Token generado correctamente.");
        return token;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // --- SECCIÓN DE DEPURACIÓN 3: REGISTROS AL VALIDAR EL TOKEN ---
    public boolean validateJwtToken(String authToken) {
        logger.warn("--- INTENTANDO VALIDAR TOKEN RECIBIDO ---");
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            logger.info("ÉXITO: La firma del token es válida y no ha expirado.");
            return true;
        } catch (SignatureException e) {
            logger.error("FALLO DE VALIDACIÓN: La firma del JWT no coincide. ¿La 'jwtSecret' en application.properties es la correcta y está en Base64? Error: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("FALLO DE VALIDACIÓN: Token JWT malformado. Error: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("FALLO DE VALIDACIÓN: El token JWT ha expirado. Error: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("FALLO DE VALIDACIÓN: Token JWT no soportado. Error: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("FALLO DE VALIDACIÓN: El contenido del token está vacío. Error: {}", e.getMessage());
        }
        logger.warn("-----------------------------------------");
        return false;
    }
}