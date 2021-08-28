package com.deofis.tiendaapirest.compras.services;

import com.deofis.tiendaapirest.clientes.entities.Cliente;
import com.deofis.tiendaapirest.operaciones.entities.EstadoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.EventoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;
import com.deofis.tiendaapirest.operaciones.statemachine.StateMachineService;
import com.deofis.tiendaapirest.perfiles.services.PerfilService;
import lombok.AllArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AnuladorCompra implements AnularCompraService {

    private final PerfilService perfilService;
    private  final EncontradorCompraCliente encontradorCompraCliente;
    private final StateMachineService stateMachineService;

    @Transactional
    @Override
    public Operacion anular(Long nroOperacion) {
        Cliente clienteActual = this.clientePerfil();
        Operacion operacion = this.encontradorCompraCliente.encontrarCompraCliente(nroOperacion, clienteActual);

        if (!operacion.getEstado().equals(EstadoOperacion.EN_RESERVA))
            throw new OperacionException("No se pueden anular compras que no se encuentre en estado: EN_ENTREGA");

        StateMachine<EstadoOperacion, EventoOperacion> sm = this.stateMachineService.build(nroOperacion);

        sm.getExtendedState().getVariables().put("operacion", operacion);
        sm.getExtendedState().getVariables().put("useremail", operacion.getCliente().getEmail());

        this.stateMachineService.enviarEvento(nroOperacion, sm, EventoOperacion.ANULAR);
        return operacion;
    }

    /**
     * Encuentra el {@link Cliente} del perfil en la sesión actual.
     * @return {@link Cliente}.
     */
    private Cliente clientePerfil() {
        return this.perfilService.obtenerDatosCliente();
    }
}
