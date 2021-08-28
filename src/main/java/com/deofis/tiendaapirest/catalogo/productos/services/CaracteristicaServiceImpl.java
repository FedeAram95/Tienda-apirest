package com.deofis.tiendaapirest.catalogo.productos.services;

import com.deofis.tiendaapirest.catalogo.productos.entities.Caracteristica;
import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.productos.repositories.CaracteristicaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CaracteristicaServiceImpl implements CaracteristicaService {

    private final CaracteristicaRepository caracteristicaRepository;

    @Override
    public Caracteristica crearCaracteristica(Caracteristica caracteristica) {
        return this.save(caracteristica);
    }

    @Override
    public Caracteristica obtenerCaracteristica(Long caracteristicaId) {
        return this.findById(caracteristicaId);
    }

    @Override
    public void eliminarCaracteristica(Long caracteristicaId) {
        this.deleteById(caracteristicaId);
    }

    @Override
    public List<Caracteristica> findAll() {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public Caracteristica findById(Long aLong) {
        return this.caracteristicaRepository.findById(aLong)
                .orElseThrow(() -> new ProductoException("No existe la característica con id: " + aLong));
    }

    @Transactional
    @Override
    public Caracteristica save(Caracteristica object) {
        return this.caracteristicaRepository.save(object);
    }

    @Transactional
    @Override
    public void delete(Caracteristica object) {
        this.caracteristicaRepository.delete(object);
    }

    @Transactional
    @Override
    public void deleteById(Long aLong) {
        Caracteristica caracteristica = this.findById(aLong);

        this.delete(caracteristica);
    }
}
