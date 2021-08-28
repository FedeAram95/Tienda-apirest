package com.deofis.tiendaapirest.autenticacion.services;

import com.deofis.tiendaapirest.autenticacion.entities.Usuario;
import com.deofis.tiendaapirest.autenticacion.exceptions.AutenticacionException;
import com.deofis.tiendaapirest.autenticacion.repositories.UsuarioRepository;
import com.deofis.tiendaapirest.autenticacion.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

        Usuario usuario = this.usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AutenticacionException("Usuario no encontrado con " +
                        "username: " + userEmail));

        return UserPrincipal.create(usuario);
    }

    /*
    private Collection<? extends GrantedAuthority> getAuthorities(Rol rol) {
        return Collections.singletonList(new SimpleGrantedAuthority(rol.getNombre()));
    }

     */
}
