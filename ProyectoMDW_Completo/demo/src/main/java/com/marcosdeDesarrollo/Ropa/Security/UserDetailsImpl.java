package com.marcosdeDesarrollo.Ropa.Security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marcosdeDesarrollo.Ropa.Persistencia.Entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Integer id;
    private final String email;

    @JsonIgnore
    private String password;

    private final GrantedAuthority authority;

    public UserDetailsImpl(Integer id, String email, String password, GrantedAuthority authority) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authority = authority;
    }

    public static UserDetailsImpl build(Usuario usuario) {
        // Aquí tomamos el nombre del rol (ej: "ROLE_ADMIN") y lo convertimos
        // en un objeto que Spring Security entiende.
        GrantedAuthority authority = new SimpleGrantedAuthority(usuario.getRol().getNombre());

        return new UserDetailsImpl(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getPassword(),
                authority);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security usará este método para saber el rol del usuario.
        return List.of(authority);
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        // Usamos el email como "username" para la autenticación.
        return email;
    }

    // Los siguientes métodos los dejamos en 'true' para un control simple.
    // Sirven para deshabilitar cuentas, bloquearlas, etc.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}