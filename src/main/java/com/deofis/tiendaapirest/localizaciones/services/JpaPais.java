package com.deofis.tiendaapirest.localizaciones.services;

import com.deofis.tiendaapirest.localizaciones.entities.Pais;
import com.deofis.tiendaapirest.localizaciones.exceptions.LocalizationException;
import com.deofis.tiendaapirest.localizaciones.repositories.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación utilizando JPA de {@link PaisDSGateway}.
 */
@Service
public class JpaPais implements PaisDSGateway {

    private final PaisRepository paisRepository;

    @Autowired
    public JpaPais(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Pais> findAll() {
        return this.paisRepository.findAllByOrderByNombreAsc();
    }

    @Transactional(readOnly = true)
    @Override
    public Pais findById(Long paisId) {
        return this.paisRepository.findById(paisId)
                .orElseThrow(() -> new LocalizationException("País no encontrado con id: " + paisId));
    }

    @Transactional(readOnly = true)
    @Override
    public Pais findByNombre(String pais) {
        return this.paisRepository.findByNombre(pais)
                .orElseThrow(() -> new LocalizationException("País no encontrado con nombre: " + pais));
    }

    @Transactional(readOnly = true)
    @Override
    public Pais findByCodigo(String code) {
        return this.paisRepository.findByIso2(code)
                .orElseThrow(() -> new LocalizationException("País no encontrado con código: " + code));
    }
}
