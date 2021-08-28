package com.deofis.tiendaapirest.operaciones.dto;

import com.deofis.tiendaapirest.clientes.entities.Direccion;
import com.deofis.tiendaapirest.operaciones.entities.DetalleOperacion;
import com.deofis.tiendaapirest.pagos.entities.MedioPago;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperacionRequest {
    private Long nroOperacion;
    private DetalleOperacion item;
    private Direccion direccionEnvio;
    private MedioPago medioPago;
}
