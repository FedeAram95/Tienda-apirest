package com.deofis.tiendaapirest.pagos.cuentasbancarias.services;

import com.deofis.tiendaapirest.localizaciones.entities.Pais;
import com.deofis.tiendaapirest.localizaciones.services.PaisDSGateway;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.entities.CuentaBancaria;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.exceptions.CuentaBancariaException;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.repositories.CuentaBancariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CuentaBancariaDSGatewayImpl implements CuentaBancariaDSGateway {

    private final CuentaBancariaRepository cuentaBancariaRepository;
    private final PaisDSGateway paisDSGateway;

    @Autowired
    public CuentaBancariaDSGatewayImpl(CuentaBancariaRepository cuentaBancariaRepository, PaisDSGateway paisDSGateway) {
        this.cuentaBancariaRepository = cuentaBancariaRepository;
        this.paisDSGateway = paisDSGateway;
    }

    @Transactional
    @Override
    public CuentaBancaria save(CuentaBancaria cuentaBancaria) {
        return this.cuentaBancariaRepository.save(cuentaBancaria);
    }

    @Transactional(readOnly = true)
    @Override
    public CuentaBancaria findById(Long cuentaId) throws CuentaBancariaException {
        return this.cuentaBancariaRepository.findById(cuentaId)
                .orElseThrow(() -> new CuentaBancariaException("No existe la cuenta bancaria con id: " + cuentaId));
    }

    @Transactional(readOnly = true)
    @Override
    public CuentaBancaria findByPrincipal() throws CuentaBancariaException {
        return this.cuentaBancariaRepository.findFirstByPrincipal(true)
                .orElseThrow(() -> new CuentaBancariaException("No existe una cuenta bancaria principal"));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByPais(Pais pais) {
        return this.cuentaBancariaRepository.existsByPais(pais);
    }

    @Transactional(readOnly = true)
    @Override
    public CuentaBancaria findByPais(Long paisId) throws CuentaBancariaException {
        // excp handling y obtencion de pais deberia ser impl por paisDsGateway, cuando
        // se rediseñe la arq. incluir este cambio.
        Pais pais = this.paisDSGateway.findById(paisId);

        return this.cuentaBancariaRepository.findByPais(pais)
                .orElseThrow(() -> new CuentaBancariaException("No existe una cuenta asociada al pais: " +
                        pais.getNombre()));
    }

    @Transactional(readOnly = true)
    @Override
    public Pais findPaisById(Long paisId) {
        return this.paisDSGateway.findById(paisId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CuentaBancaria> findAll() {
        return this.cuentaBancariaRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteById(Long cuentaId) throws CuentaBancariaException {
        CuentaBancaria cuentaAEliminar = this.findById(cuentaId);
        this.cuentaBancariaRepository.delete(cuentaAEliminar);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CuentaBancaria> findFirst() {
        return this.cuentaBancariaRepository.findFirstByPrincipal(false);
    }
}
