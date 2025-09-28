package com.marcosdeDesarrollo.demo.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    private final UserDetailsServiceImpl userDetailsService;

    public static Logger getLogger() {
        return LoggerFactory.getLogger(AuthTokenFilter.class);
    }

    AuthTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    // ELIMINAMOS POR COMPLETO LA LÓGICA DE 'excludedUrls' y 'shouldNotFilter'.
    // La configuración de seguridad en WebSecurityConfig ya se encarga de esto.
    // Nuestro filtro ahora se ejecutará en las rutas protegidas, que es lo que queremos.

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Log para ver qué ruta está procesando el filtro
        LoggerFactory.getLogger(AuthTokenFilter.class).info("AuthTokenFilter procesando la ruta: {}", request.getRequestURI());

        try {
            String jwt = parseJwt(request);

            // Si hay un token y es válido, establecemos la autenticación.
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                LoggerFactory.getLogger(AuthTokenFilter.class).info("Usuario '{}' autenticado correctamente por token JWT.", username);
            } else {
                // Si no hay token, simplemente seguimos. Si la ruta es protegida,
                // Spring Security la bloqueará más adelante.
                LoggerFactory.getLogger(AuthTokenFilter.class).warn("No se encontró un token JWT válido en la petición a {}", request.getRequestURI());
            }
        } catch (Exception e) {
            LoggerFactory.getLogger(AuthTokenFilter.class).error("No se pudo establecer la autenticación del usuario: {}", e.getMessage());
        }

        // ¡CRUCIAL! Continuar con la cadena de filtros.
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}