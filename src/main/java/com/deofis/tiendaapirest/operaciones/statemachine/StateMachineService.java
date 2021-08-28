package com.deofis.tiendaapirest.operaciones.statemachine;

import com.deofis.tiendaapirest.operaciones.entities.EstadoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.EventoOperacion;
import org.springframework.statemachine.StateMachine;

public interface StateMachineService {

    StateMachine<EstadoOperacion, EventoOperacion> build(Long nroOperacion);

    void enviarEvento(Long nroOperacion, StateMachine<EstadoOperacion, EventoOperacion> sm, EventoOperacion evento);
}
