package com.deofis.tiendaapirest.compras.dto;

import com.deofis.tiendaapirest.clientes.entities.Direccion;
import com.deofis.tiendaapirest.operaciones.entities.DetalleOperacion;
import com.deofis.tiendaapirest.operaciones.entities.EstadoOperacion;
import com.deofis.tiendaapirest.pagos.entities.MedioPago;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompraPayload {
    private Long nroOperacion;
    private Date fechaOperacion;
    private Date fechaEnvio;
    private Date fechaEntrega;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private EstadoOperacion estado;
    private String cliente;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Direccion direccionEnvio;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MedioPago medioPago;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<DetalleOperacion> items;
    private Double total;
}
