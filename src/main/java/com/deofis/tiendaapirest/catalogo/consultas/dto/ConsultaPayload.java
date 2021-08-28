package com.deofis.tiendaapirest.catalogo.consultas.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ConsultaPayload {

    @NotNull(message = "Tu email es obligatorio")
    private String email;
    private String telefono;
    @NotNull(message = "El mensaje es obligatorio")
    private String mensaje;
    @NotNull(message = "Id del producto requerido")
    private Long productoId;

    public ConsultaPayload() {

    }

    public ConsultaPayload(String email, String telefono, String mensaje, Long productoId) {
        this.email = email;
        this.telefono = telefono;
        this.mensaje = mensaje;
        this.productoId = productoId;
    }
}
