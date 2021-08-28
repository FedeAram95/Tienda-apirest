package com.deofis.tiendaapirest.catalogo.consultas.services;

import com.deofis.tiendaapirest.catalogo.consultas.dto.ConsultaPayload;

public interface ConsultaService {

    /**
     * Servicio para enviar mensaje de consulta sobre cierto producto.
     * @param consultaPayload {@link ConsultaPayload} con los datos para la consulta.
     */
    void enviarConsulta(ConsultaPayload consultaPayload);

}
