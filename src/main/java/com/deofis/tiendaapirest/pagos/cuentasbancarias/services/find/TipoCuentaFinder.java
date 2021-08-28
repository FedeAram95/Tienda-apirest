package com.deofis.tiendaapirest.pagos.cuentasbancarias.services.find;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.dto.TipoCuentaBancariaDto;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.entities.TipoCuentaBancaria;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoCuentaFinder implements TipoCuentaFinderService {

    @Override
    public List<TipoCuentaBancariaDto> listarTiposCuenta() {
        return Arrays.stream(TipoCuentaBancaria.values())
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Mapea enum a dto para la response.
     * @param tipoCuentaBancaria {@link TipoCuentaBancaria} enum con tipos existentes (fijo).
     * @return {@link TipoCuentaBancariaDto}.
     */
    private TipoCuentaBancariaDto mapToDto(TipoCuentaBancaria tipoCuentaBancaria) {
        return TipoCuentaBancariaDto.builder()
                .tipo(tipoCuentaBancaria.name()).build();
    }
}
