package com.deofis.tiendaapirest.operaciones.services.completador;

import com.deofis.tiendaapirest.operaciones.entities.EstadoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.EventoOperacion;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.repositories.OperacionRepository;
import com.deofis.tiendaapirest.operaciones.services.completador.consts.OperacionCompletadorConsts;
import com.deofis.tiendaapirest.operaciones.statemachine.StateMachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Servicio programable se ejecuta por defecto a las 01:00 AM hora del servidor.
 */
@Service
public class OperacionCompletadorImpl implements OperacionCompletador {

    private final Logger log = LoggerFactory.getLogger(OperacionCompletadorImpl.class);
    private final OperacionRepository operacionRepository;
    private final StateMachineService stateMachineService;

    @Autowired
    public OperacionCompletadorImpl(OperacionRepository operacionRepository, StateMachineService stateMachineService) {
        this.operacionRepository = operacionRepository;
        this.stateMachineService = stateMachineService;
    }

    @Transactional
    @Override
    public void completarOperacion(Operacion operacion) {
        log.info("Completando operacion "+ operacion.getNroOperacion());

        StateMachine<EstadoOperacion, EventoOperacion> sm = this.stateMachineService.build(operacion.getNroOperacion());
        sm.getExtendedState().getVariables().put("operacion", operacion);
        sm.getExtendedState().getVariables().put("useremail", operacion.getCliente().getEmail());

        // transitar estado final COMPLETADO
        this.stateMachineService.enviarEvento(operacion.getNroOperacion(), sm, EventoOperacion.COMPLETAR);
        log.info("Operación completada: " + operacion);
    }

    @Scheduled(cron = "0 0 1 1/1 * ?")
    @Transactional
    @Override
    public void execute() {
        List<Operacion> actualEntregados = this.findAllEntregado();
        List<Operacion> operacionesACompletar = this.filtrarEntregados(actualEntregados);

        for (Operacion operacion: operacionesACompletar) {
            this.completarOperacion(operacion);
        }

        log.info("Completadas: " + operacionesACompletar.size());
    }

    /**
     * Busca y devuelve, de la base de datos, todas las operaciones que se encuentran en estado
     * ENTREGADO.
     * @return List con operaciones entregadas.
     */
    @Transactional(readOnly = true)
    public List<Operacion> findAllEntregado() {
        return this.operacionRepository.findAllByEstado(EstadoOperacion.ENTREGADO);
    }

    /**
     * Filtra las operaciones que recibe, devolviendo solo aquellas que cumplan: los dias pasados
     * de fecha entrega superan X cantidad de tiempo (dias).
     * @param aFiltrar {@link List<Operacion>} operaciones a filtrar.
     * @return {@link List<Operacion>} operaciones filtradas: superan fecha entrega establecida.
     */
    private List<Operacion> filtrarEntregados(List<Operacion> aFiltrar) {
        Integer maxDias = OperacionCompletadorConsts.DIAS_ENTREGADO_MAX;

        aFiltrar = aFiltrar
                .stream()
                .filter(op -> maxDias < calcularDiff(op.getFechaEntrega()))
                .collect(Collectors.toList());

        return aFiltrar;
    }

    /**
     * Calcula la diferencia entre una fecha requerida y la fecha actual del sistema.
     * @param fecha {@link Date} a comparar con fecha actual.
     * @return Integer cantidad de días.
     */
    private Integer calcularDiff(Date fecha) {
        long fechaMillis = fecha.getTime();
        long currentMillis = System.currentTimeMillis();

        long diff = currentMillis - fechaMillis;
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
