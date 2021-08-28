package com.deofis.tiendaapirest.autenticacion.services;

import com.deofis.tiendaapirest.autenticacion.entities.RefreshToken;
import com.deofis.tiendaapirest.autenticacion.exceptions.AutenticacionException;
import com.deofis.tiendaapirest.autenticacion.repositories.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


@Service
@AllArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    @Override
    public RefreshToken generarRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiration(expiration());
        refreshToken.setFechaCreacion(new Date());

        return this.refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void extenderRefreshToken(String token) {
        RefreshToken tokenActual = this.refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new AutenticacionException("Refresh Token Inválido"));

        // Si el refresh token expiró, extendemos su tiempo de uso
        log.info("extendiendo refresh token...");
        tokenActual.setExpiration(expiration());
        this.refreshTokenRepository.save(tokenActual);
    }

    @Transactional(readOnly = true)
    @Override
    public void validarRefreshToken(String token) {
        this.refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new AutenticacionException("Refresh Token Inválido"));
    }

    @Transactional
    @Override
    public void eliminarRefreshToken(String token) {
        this.refreshTokenRepository.deleteByToken(token);
    }

    /**
     * Genera expiration date para los refresh tokens. Por defecto: 1 día.
     * @return Date expiration.
     */
    private Date expiration() {
        Date expiration = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(expiration);
        calendar.add(Calendar.DATE, 1);

        expiration = calendar.getTime();
        return expiration;
    }
}
