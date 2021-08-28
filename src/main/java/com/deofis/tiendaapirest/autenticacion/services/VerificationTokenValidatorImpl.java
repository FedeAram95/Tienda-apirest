package com.deofis.tiendaapirest.autenticacion.services;

import com.deofis.tiendaapirest.autenticacion.entities.VerificationToken;
import com.deofis.tiendaapirest.autenticacion.repositories.VerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Este servicio automático se encarga de validar, todos los dias del mes, a las 00:00HS, los
 * tokens de verificación, y elimina aquellos que han expirado al momento de la validación.
 */
@Service
public class VerificationTokenValidatorImpl implements VerificationTokenValidator {

    private final Logger log = LoggerFactory.getLogger(VerificationTokenValidatorImpl.class);
    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public VerificationTokenValidatorImpl(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public boolean expired(VerificationToken verificationToken) {
        Date actualDate = new Date();
        return actualDate.after(verificationToken.getExpiraEn());
    }

    @Override
    public void delete(VerificationToken verificationToken) {
        // Lógica de elimnación de verification tokens...
        log.info("### Eliminando de la Base de Datos... ###");
    }

    @Override
    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void execute() {
        List<VerificationToken> all = this.verificationTokenRepository.findAll();
        if (all.size() == 0) return;

        int total = 0;
        for (VerificationToken verificationToken : all) {
            if (expired(verificationToken)) {
                delete(verificationToken);
                total++;
            }
        }

        log.info("Total eliminados: " + total );
    }
}
