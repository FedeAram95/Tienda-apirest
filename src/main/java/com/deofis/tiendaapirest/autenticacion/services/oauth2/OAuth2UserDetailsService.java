package com.deofis.tiendaapirest.autenticacion.services.oauth2;

import com.deofis.tiendaapirest.autenticacion.entities.AuthProvider;
import com.deofis.tiendaapirest.autenticacion.entities.Rol;
import com.deofis.tiendaapirest.autenticacion.entities.Usuario;
import com.deofis.tiendaapirest.autenticacion.exceptions.OAuth2AuthenticationProcessingException;
import com.deofis.tiendaapirest.autenticacion.repositories.RolRepository;
import com.deofis.tiendaapirest.autenticacion.repositories.UsuarioRepository;
import com.deofis.tiendaapirest.autenticacion.security.UserPrincipal;
import com.deofis.tiendaapirest.autenticacion.security.oauth2.user.OAuth2UserInfo;
import com.deofis.tiendaapirest.autenticacion.security.oauth2.user.OAuth2UserInfoFactory;
import com.deofis.tiendaapirest.clientes.entities.Cliente;
import com.deofis.tiendaapirest.perfiles.services.PerfilService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class OAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PerfilService perfilService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(userRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes());

        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email no encontrado por el proovedor");
        }

        log.info(oAuth2UserInfo.getAttributes().toString());

        Optional<Usuario> usuarioOptional = this.usuarioRepository.findByEmail(oAuth2UserInfo.getEmail());
        Usuario usuario;

        if (usuarioOptional.isPresent()) {
            usuario = usuarioOptional.get();

            if (!usuario.getAuthProvider().equals(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Tu%20email%20esta%20asociado%20con%20una%20cuenta%20"
                        + usuario.getAuthProvider() + ".Porfavor,%20utiliza%20tus%20credenciales%20" + usuario.getAuthProvider() + "%20para" +
                        "%20inciar%20sesion.");
            }

        } else {
            usuario = registrarUsuario(userRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(usuario, oAuth2User.getAttributes());
    }

    private Usuario registrarUsuario(OAuth2UserRequest userRequest, OAuth2UserInfo userInfo) {
        Rol role_user = this.rolRepository.findByNombre("ROLE_USER").orElseThrow(() ->
                new OAuth2AuthenticationProcessingException("Rol usuario no cargado en la bd"));

        Usuario usuario = Usuario.builder()
                .email(userInfo.getEmail())
                .authProvider(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId()))
                .providerId(userInfo.getId())
                .fechaCreacion(new Date())
                .enabled(true)
                .rol(role_user)
                .build();

        log.info("First Name: " + userInfo.getFirstName());
        log.info("Last Name: " + userInfo.getLastName());
        Cliente cliente = Cliente.builder()
                .email(userInfo.getEmail())
                .nombre(userInfo.getFirstName())
                .apellido(userInfo.getLastName())
                .build();

        this.perfilService.cargarPerfil(cliente, usuario);

        return this.usuarioRepository.save(usuario);
    }
}
