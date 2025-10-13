package com.marcosdeDesarrollo.Ropa.Security;

import com.marcosdeDesarrollo.Ropa.Persistencia.Entity.Usuario;
import com.marcosdeDesarrollo.Ropa.Domain.Repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    final
    UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca el usuario en la base de datos por su email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + email));

        // Usa el "traductor" (UserDetailsImpl) que creamos antes para convertir
        // tu objeto Usuario a un objeto que Spring Security entiende.
        return UserDetailsImpl.build(usuario);
    }
}