package com.deofis.tiendaapirest.autenticacion.services;

import com.deofis.tiendaapirest.autenticacion.dto.CambiarPasswordRequest;
import com.deofis.tiendaapirest.autenticacion.entities.Usuario;
import com.deofis.tiendaapirest.autenticacion.entities.VerificationToken;
import com.deofis.tiendaapirest.autenticacion.exceptions.AutenticacionException;
import com.deofis.tiendaapirest.autenticacion.repositories.UsuarioRepository;
import com.deofis.tiendaapirest.emails.dto.SimpleNotificacionEmail;
import com.deofis.tiendaapirest.emails.services.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordServiceImpl implements PasswordService {

    private final AutenticacionService autenticacionService;
    private final UsuarioRepository usuarioRepository;
    private final VerificationTokenService verificationTokenService;
    private final PasswordEncoder passwordEncoder;
    private final String clientUrl;

    @Qualifier("simpleMailService") private final MailService mailService;

    @Transactional
    @Override
    public Usuario cambiarPassword(CambiarPasswordRequest passwordRequest) {

        if (this.autenticacionService.estaLogueado()) {
            log.info("LOGUEADO");

            Usuario usuario = this.autenticacionService.getUsuarioActual();
            usuario.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
            return this.usuarioRepository.save(usuario);
        } else {
            log.info("NO LOGUEADO");
            throw new AutenticacionException("Usuario no logueado en el sistema");
        }

    }

    @Transactional
    @Override
    public Usuario cambiarPassword(String token, CambiarPasswordRequest cambiarPasswordRequest) {
        VerificationToken verificationToken = this.verificationTokenService.getVerificationToken(token);

        String usuarioEmail = verificationToken.getUsuario().getEmail();
        Usuario usuarioCambiarPass = this.usuarioRepository.findByEmail(usuarioEmail)
                .orElseThrow(() -> new AutenticacionException("No se encontro el usuario: " + usuarioEmail));

        usuarioCambiarPass.setPassword(passwordEncoder.encode(cambiarPasswordRequest.getPassword()));
        this.verificationTokenService.delete(verificationToken);

        return this.usuarioRepository.save(usuarioCambiarPass);
    }

    @Transactional
    @Override
    public void recuperarPassword(String userEmail) {
        Usuario usuario = this.usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AutenticacionException("No se encontro el usuario: " + userEmail));

        String token = this.verificationTokenService.generarVerificationToken(usuario);
        this.enviarEmail(userEmail, token);
    }

    private void enviarEmail(String userEmail, String token) {
        String title = "Restablece tu contraseña";
        String bodyMessage = "¡No te preocupes!, puedes restablecer tu contraseña haciendo click en el siguiente enlace:";
        String redirectUrl = this.clientUrl.concat("/recuperar-password/").concat(token);
        String subject = "Recuperar contraseña Wantfrom";

        SimpleNotificacionEmail notificacionEmail = SimpleNotificacionEmail.builder()
                .title(title)
                .body(bodyMessage)
                .subject(subject)
                .redirectUrl(redirectUrl)
                .recipient(userEmail).build();

        this.mailService.sendEmail(notificacionEmail);
    }
}
