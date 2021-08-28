package com.deofis.tiendaapirest.pagos.services;

import com.deofis.tiendaapirest.pagos.entities.MedioPago;
import com.deofis.tiendaapirest.pagos.exceptions.MedioPagoException;
import com.deofis.tiendaapirest.pagos.repositories.MedioPagoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class MedioPagoServiceImpl implements MedioPagoService {

    private final MedioPagoRepository medioPagoRepository;

    @Transactional(readOnly = true)
    @Override
    public List<MedioPago> findAll() {
        return this.medioPagoRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public MedioPago findById(Long aLong) {
        return this.medioPagoRepository.findById(aLong)
                .orElseThrow(() -> new MedioPagoException("No existe medio de pago con id: " + aLong));
    }

    @Override
    public MedioPago save(MedioPago object) {
        return null;
    }

    @Override
    public void delete(MedioPago object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
