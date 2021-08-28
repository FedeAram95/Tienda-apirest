package com.deofis.tiendaapirest.pagos.cuentasbancarias.services.paises;

import com.deofis.tiendaapirest.localizaciones.entities.Pais;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.dto.PaisDto;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.entities.CuentaBancaria;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.services.CuentaBancariaDSGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaisesUsadosFinderImpl implements PaisesUsadosFinder {

    private final CuentaBancariaDSGateway cuentaBancariaDSGateway;

    @Autowired
    public PaisesUsadosFinderImpl(CuentaBancariaDSGateway cuentaBancariaDSGateway) {
        this.cuentaBancariaDSGateway = cuentaBancariaDSGateway;
    }

    @Override
    public List<PaisDto> findPaisesEnCuentas() {
        List<CuentaBancaria> cuentas = this.cuentaBancariaDSGateway.findAll();
        List<Pais> paisesCuentas = new ArrayList<>();

        for (CuentaBancaria cuentaBancaria: cuentas) {
            // Como un req. es que no se puedan repetir cuentas del mismo país, estamos seguros
            // que no habrá duplicados en nuestro array list de paisesCuenta.
            paisesCuentas.add(cuentaBancaria.getPais());
        }
        return paisesCuentas
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PaisDto mapToDto(Pais pais) {
        return PaisDto.builder()
                .id(pais.getId())
                .nombre(pais.getNombre())
                .codigo(pais.getIso2()).build();
    }
}
