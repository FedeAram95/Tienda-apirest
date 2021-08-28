package com.deofis.tiendaapirest.localizaciones.services;

import com.deofis.tiendaapirest.localizaciones.entities.Ciudad;
import com.deofis.tiendaapirest.localizaciones.entities.Estado;
import com.deofis.tiendaapirest.localizaciones.entities.Pais;
import com.deofis.tiendaapirest.localizaciones.exceptions.LocalizationException;
import com.deofis.tiendaapirest.localizaciones.facade.PaisResponse;
import com.deofis.tiendaapirest.localizaciones.repositories.EstadoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LocalizacionServiceImpl implements LocalizacionService {

    private final PaisDSGateway paisDSGateway;
    private final EstadoRepository estadoRepository;

    @Override
    public List<Pais> listarPaises() {
        return this.paisDSGateway.findAll();
    }

    @Override
    public Pais obtenerPais(Long id) {
        return this.paisDSGateway.findById(id);
    }

    @Override
    public Pais obtenerPais(String nombrePais) {
        return this.paisDSGateway.findByNombre(nombrePais);
    }

    @Override
    public PaisResponse obtenerPaisResponse(String nombrePais) {
        return this.mapToResponse(this.paisDSGateway.findByNombre(nombrePais));
    }

    @Override
    public PaisResponse obtenerPaisByCodigo(String codigo) {
        return this.mapToResponse(this.paisDSGateway.findByCodigo(codigo));
    }

    @Override
    public List<Estado> estadosDePais(Long paisId) {
        Pais pais = this.obtenerPais(paisId);

        return pais.getEstados();
    }

    @Override
    public List<Estado> estadosDePais(String nombrePais) {
        Pais pais = this.obtenerPais(nombrePais);

        return pais.getEstados();
    }

    @Override
    public Estado obtenerEstado(String nombreEstado) {
        return this.estadoRepository.findByNombre(nombreEstado)
                .orElseThrow(() -> new LocalizationException("Estado no encontrado con nombre: " + nombreEstado));
    }

    @Override
    public Estado obtenerEstado(Long estadoId) {
        return this.estadoRepository.findById(estadoId)
                .orElseThrow(() -> new LocalizationException("Estado no encontrado con id: " + estadoId));
    }

    @Override
    public List<Ciudad> ciudadesEstado(String nombrePais, String nombreEstado) {
        Pais pais = this.obtenerPais(nombrePais);
        Estado estado = null;

        for (Estado state: pais.getEstados()) {
            if (state.getNombre().equals(nombreEstado)) estado = state;
        }

        if (estado == null) throw new LocalizationException("El estado: " + nombreEstado +
                " no pertenece al país: " + nombreEstado);


        return estado.getCiudades();
    }

    @Override
    public List<Ciudad> ciudadesEstado(Long paisId, Long estadoId) {
        Pais pais = this.obtenerPais(paisId);
        Estado estado = null;

        for (Estado state: pais.getEstados()) {
            if (state.getId().equals(estadoId)) estado = state;
        }

        if (estado == null) throw new LocalizationException("El estado: " + estadoId +
                " no pertenece al país: " + paisId);


        return estado.getCiudades();
    }

    /**
     * Mapea clase entidad de pais, a una clase response.
     * TODO: cambiar todas las respuestas del backend a la clase {@link PaisResponse} (obvio cuando haya tiempo...)
     * @param paisEntity {@link Pais} entidad.
     * @return {@link PaisResponse} response.
     */
    private PaisResponse mapToResponse(Pais paisEntity) {
        return PaisResponse.builder()
                .id(paisEntity.getId())
                .nombre(paisEntity.getNombre())
                .codigo(paisEntity.getIso2()).build();
    }
}
