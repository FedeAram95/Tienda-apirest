package com.deofis.tiendaapirest.autenticacion.controllers;

import com.deofis.tiendaapirest.autenticacion.dto.CambiarPasswordRequest;
import com.deofis.tiendaapirest.autenticacion.entities.Usuario;
import com.deofis.tiendaapirest.autenticacion.exceptions.AutenticacionException;
import com.deofis.tiendaapirest.autenticacion.exceptions.MailSenderException;
import com.deofis.tiendaapirest.autenticacion.services.PasswordService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * API que se dedica al cambio/reinicio de contraseña de los usuarios del sistema.
 */

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    private final String clientUrl;

    /**
     * Cambiar contraseña de un usuario ya logueado en la App.
     * URL: ~/api/auth/cambiar-password
     * HttpMethod: POST
     * HttpStatus: OK
     * @param passwordRequest CambiarPasswordRequest tiene los datos de la contraseña.
     *                        Usamos este objeto para no pasar la contraseña por un RequestParam.
     * @return ResponseEntity con un mensaje de éxito/error.
     */
    @PostMapping("/cambiar-password")
    public ResponseEntity<String> cambiarPassword(@RequestBody CambiarPasswordRequest passwordRequest) {

        Usuario usuario;
        try {
            usuario = this.passwordService.cambiarPassword(passwordRequest);
            return new ResponseEntity<>("Contraseña cambiada con éxito. Email del usuario: "
                    + usuario.getEmail(), HttpStatus.OK);
        } catch (AutenticacionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Cambiar la contraseña de un usuario que la olvidó.
     * URL: ~/api/auth/cambiar-password/aaaa-bbbbb-1111-2222
     * HttpMethod: POST
     * HttpStatus: OK
     * @param cambiarPasswordRequest password nueva.
     * @param token token del usuario que pidió nueva contraseña.
     * @return ResponseEntity con mensaje éxito/error.
     */
    @PostMapping("/cambiar-password/{token}")
    public ResponseEntity<String> cambiarPassword(@PathVariable String token,
                                                  @RequestBody CambiarPasswordRequest cambiarPasswordRequest) {
        Usuario usuario;

        try {
            usuario = this.passwordService.cambiarPassword(token, cambiarPasswordRequest);
        } catch (AutenticacionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Contraseña cambiada con éxito al usuario: " +
                usuario.getEmail(), HttpStatus.OK);
    }

    /**
     * Inicia la recuperación de password enviando email al usuario correspondiente.
     * URL: ~/api/auth/recuperar-password
     * HttpMethod: GET
     * HttpStatus: OK
     * @param email String email del usuario.
     * @return ResponseEntity con mensaje éxito/error.
     */
    @GetMapping("/recuperar-password")
    public ResponseEntity<String> recuperarPassword(@RequestParam String email) {

        try {
            this.passwordService.recuperarPassword(email);
        } catch (AutenticacionException | MailSenderException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Email enviado para recuperar contraseña.", HttpStatus.OK);
    }
}
