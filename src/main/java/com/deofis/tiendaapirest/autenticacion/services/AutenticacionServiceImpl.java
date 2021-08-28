package com.deofis.tiendaapirest.autenticacion.services;

import com.deofis.tiendaapirest.autenticacion.dto.AuthResponse;
import com.deofis.tiendaapirest.autenticacion.dto.IniciarSesionRequest;
import com.deofis.tiendaapirest.autenticacion.dto.RefreshTokenRequest;
import com.deofis.tiendaapirest.autenticacion.dto.SignupRequest;
import com.deofis.tiendaapirest.autenticacion.entities.AuthProvider;
import com.deofis.tiendaapirest.autenticacion.entities.Rol;
import com.deofis.tiendaapirest.autenticacion.entities.Usuario;
import com.deofis.tiendaapirest.autenticacion.entities.VerificationToken;
import com.deofis.tiendaapirest.autenticacion.exceptions.AutenticacionException;
import com.deofis.tiendaapirest.autenticacion.exceptions.PasswordException;
import com.deofis.tiendaapirest.autenticacion.repositories.RolRepository;
import com.deofis.tiendaapirest.autenticacion.repositories.UsuarioRepository;
import com.deofis.tiendaapirest.autenticacion.security.JwtProveedor;
import com.deofis.tiendaapirest.autenticacion.security.UserPrincipal;
import com.deofis.tiendaapirest.emails.dto.SimpleNotificacionEmail;
import com.deofis.tiendaapirest.emails.services.MailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@AllArgsConstructor
@Slf4j
public class AutenticacionServiceImpl implements AutenticacionService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final VerificationTokenService verificationTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtProveedor jwtProveedor;
    private final RefreshTokenService refreshTokenService;
    private final String baseUrl;

    @Qualifier("simpleMailService") private final MailService mailService;

    @Transactional
    @Override
    public void registrarse(SignupRequest signupRequest) {
        Rol user = this.rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new AutenticacionException("ROLE_USER no cargado."));

        if (this.usuarioRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new AutenticacionException("Ya existe el usuario con el email: " + signupRequest.getEmail() +".\n" +
                    "Si no activó su cuenta, porfavor revise su bandeja de entradas.");
        }

        if (signupRequest.getPassword().length() < 8) {
            throw new PasswordException("La contraseña debe tener al menos 8 caracteres.");
        }


        Usuario usuario = Usuario.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .enabled(false)
                .authProvider(AuthProvider.local)
                .providerId("1")
                .fechaCreacion(new Date())
                .rol(user)
                .build();

        try {
            this.usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException e) {
            throw new AutenticacionException("Excepción al registrar usuario: " + signupRequest.getEmail());
        }

        String token = this.verificationTokenService.generarVerificationToken(usuario);
        this.notificarEmail(usuario.getEmail(), token);
    }

    private void notificarEmail(String useremail, String token) {
        String title = "¡Gracias por registrarte en Wantfrom!";
        String bodyMessage = "¡Sólo queda una paso más!. Por favor, verifique su cuenta haciendo click en el " +
                "siguiente enlace.";
        String redirectUrl = this.baseUrl.concat("/api/auth/accountVerification/").concat(token);
        String subject = "Activación de cuenta Wantfrom";

        SimpleNotificacionEmail notificationEmail = SimpleNotificacionEmail.builder()
                .title(title)
                .body(bodyMessage)
                .subject(subject)
                .redirectUrl(redirectUrl)
                .recipient(useremail).build();

        this.mailService.sendEmail(notificationEmail);
    }
    
    @Override
    public void verificarCuenta(String token) {
        VerificationToken verificationToken = this.verificationTokenService.getVerificationToken(token);

        fetchUserAndEnable(verificationToken);
    }

    @Transactional
    @Override
    public AuthResponse iniciarSesion(IniciarSesionRequest iniciarSesionRequest) {
        Usuario usuario = this.usuarioRepository.findByEmail(iniciarSesionRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        if (usuario.getAuthProvider() != AuthProvider.local) {
            throw new AutenticacionException("El email que estas intentando usar está registrado como una cuenta de: "
                    + usuario.getAuthProvider().toString().toUpperCase() +
                    ". Porfavor, inicie sesión con la cuenta que se registró inicialmente : " +
                    usuario.getAuthProvider().toString().toUpperCase());
        }

        Authentication authenticate = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(iniciarSesionRequest.getEmail(),
                        iniciarSesionRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String jwtToken = this.jwtProveedor.generateToken(authenticate);

        return AuthResponse.builder()
                .authToken(jwtToken)
                .userEmail(iniciarSesionRequest.getEmail())
                .refreshToken(this.refreshTokenService.generarRefreshToken().getToken())
                .rol(usuario.getRol().getNombre())
                .expiraEn(new Date(new Date().getTime() + jwtProveedor.getExpirationInMillis()))
                .build();
    }

    @Override
    public void cerrarSesion(RefreshTokenRequest refreshTokenRequest) {
        if (this.estaLogueado()) {
            this.refreshTokenService.eliminarRefreshToken(refreshTokenRequest.getRefreshToken());
            SecurityContextHolder.getContext().setAuthentication(null);
        } else {
            log.info("NO LOGUEADO");
            throw new AutenticacionException("Usuario no logueado en el sistema");
        }
    }

    @Transactional
    @Override
    public AuthResponse refrescarToken(RefreshTokenRequest refreshTokenRequest) {
        this.refreshTokenService.validarRefreshToken(refreshTokenRequest.getRefreshToken());
        this.refreshTokenService.extenderRefreshToken(refreshTokenRequest.getRefreshToken());
        String jwt = this.jwtProveedor.generateTokenWithUsername(refreshTokenRequest.getUserEmail());

        return AuthResponse.builder()
                .authToken(jwt)
                .userEmail(refreshTokenRequest.getUserEmail())
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiraEn(new Date(new Date().getTime() + this.jwtProveedor.getExpirationInMillis()))
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public Usuario getUsuarioActual() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return this.usuarioRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new AutenticacionException("Usuario no encontrado: " +
                        principal.getUsername()));
    }

    @Override
    public boolean estaLogueado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    /**
     * Habilita una cuenta de usuario que hace match con el token de verificación.
     * @param verificationToken VerificationToken objeto token con los datos del usuario y
     *                          el token de verificación.
     */
    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {

        String userEmail = verificationToken.getUsuario().getEmail();

        Usuario usuario = this.usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AutenticacionException("No se encontro el usuario: " + userEmail));
        usuario.setEnabled(true);
        this.usuarioRepository.save(usuario);
        // Luego de verificar la cuenta, se borra el token para borrar data basura.
        this.verificationTokenService.delete(verificationToken);
    }
}
