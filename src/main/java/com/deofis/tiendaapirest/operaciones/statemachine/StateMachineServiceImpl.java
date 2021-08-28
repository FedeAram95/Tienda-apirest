package com.deofis.tiendaapirest.operaciones.statemachine;

import com.deofis.tiendaapirest.operaciones.entities.EstadoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.EventoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.exceptions.OperacionException;
import com.deofis.tiendaapirest.operaciones.repositories.OperacionRepository;
import com.deofis.tiendaapirest.operaciones.services.OperacionServiceImpl;
import com.deofis.tiendaapirest.operaciones.services.OperacionStateChangeInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StateMachineServiceImpl implements StateMachineService {

    private final StateMachineFactory<EstadoOperacion, EventoOperacion> stateMachineFactory;
    private final OperacionStateChangeInterceptor operacionStateChangeInterceptor;
    private final OperacionRepository operacionRepository;

    @Override
    public StateMachine<EstadoOperacion, EventoOperacion> build(Long nroOperacion) {
        Operacion operacion = this.operacionRepository.findById(nroOperacion)
                .orElseThrow(() -> new OperacionException("No existe la operación con id: "
                        + nroOperacion));

        StateMachine<EstadoOperacion, EventoOperacion> sm = this.stateMachineFactory.getStateMachine(Long.toString(operacion.getNroOperacion()));

        sm.stop();

        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(operacionStateChangeInterceptor);
                    sma.resetStateMachine(new DefaultStateMachineContext<>(operacion.getEstado()
                            ,null, null, null));
                });

        sm.start();

        return sm;
    }

    @Override
    public void enviarEvento(Long nroOperacion, StateMachine<EstadoOperacion, EventoOperacion> sm, EventoOperacion evento) {
        Message<EventoOperacion> msg = MessageBuilder.withPayload(evento)
                .setHeader(OperacionServiceImpl.NRO_OPERACION_HEADER, nroOperacion)
                .build();

        sm.sendEvent(msg);
    }
}
