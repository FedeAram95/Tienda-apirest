package com.deofis.tiendaapirest.pagos.cuentasbancarias.facade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class CuentaBancariaResponse implements Serializable {
    private Long id;
    private String nroCuenta;
    private String tipo;
    private String ca;
    private String alias;
    private String titular;
    private String banco;
    private String email;
    private boolean principal;
    private String pais;

    public CuentaBancariaResponse() {

    }
}
