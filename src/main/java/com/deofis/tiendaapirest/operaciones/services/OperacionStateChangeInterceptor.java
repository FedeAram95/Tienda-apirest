package com.deofis.tiendaapirest.operaciones.services;

import com.deofis.tiendaapirest.operaciones.entities.EstadoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.EventoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;
import com.deofis.tiendaapirest.operaciones.repositories.OperacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OperacionStateChangeInterceptor extends StateMachineInterceptorAdapter<EstadoOperacion, EventoOperacion> {

    private final OperacionRepository operacionRepository;

    @Override
    public void preStateChange(State<EstadoOperacion, EventoOperacion> state, Message<EventoOperacion> message,
                               Transition<EstadoOperacion, EventoOperacion> transition, StateMachine<EstadoOperacion, EventoOperacion> stateMachine) {

        Optional.ofNullable(message).ifPresent(msg -> {
            Optional.ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(OperacionServiceImpl.NRO_OPERACION_HEADER, -1L)))
                    .ifPresent(nroOperacion -> {
                        Operacion operacion = this.operacionRepository.findById(nroOperacion)
                                .orElseThrow(() -> new OperacionException("No existe la operacion con id: " + nroOperacion));
                        operacion.setEstado(state.getId());
                        this.operacionRepository.save(operacion);
                    });
        });
    }
}
