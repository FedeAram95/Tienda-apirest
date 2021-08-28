package com.deofis.tiendaapirest.pagos.cuentasbancarias.facade;

import com.deofis.tiendaapirest.pagos.cuentasbancarias.entities.CajaAhorroEnum;
import com.deofis.tiendaapirest.pagos.cuentasbancarias.entities.TipoCuentaBancaria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class CuentaBancariaUpdateRequest {
    @NotNull(message = "El n° de CBU es obligatorio")
    @Length(min = 5, message = "El n° de cuenta debe tener al menos 5 caracteres")
    private String nroCuenta;
    @NotNull(message = "El tipo de cuenta es obligatorio")
    private TipoCuentaBancaria tipo;
    @NotNull(message = "La moneda de la caja de ahorro es obligatoria")
    private CajaAhorroEnum ca;
    @Length(min = 5, message = "El alias debe tener al menos 5 caracteres")
    private String alias;
    @NotNull(message = "El nombre del titular es obligatorio")
    private String titular;
    private String banco;
    private String email;

    public CuentaBancariaUpdateRequest() {

    }
}
