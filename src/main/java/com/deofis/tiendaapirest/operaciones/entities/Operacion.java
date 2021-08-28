package com.deofis.tiendaapirest.operaciones.entities;

import com.deofis.tiendaapirest.clientes.entities.Cliente;
import com.deofis.tiendaapirest.clientes.entities.Direccion;
import com.deofis.tiendaapirest.pagos.entities.MedioPago;
import com.deofis.tiendaapirest.pagos.entities.OperacionPago;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Esta entidad hace referencia a la operación de compra/venta de la tienda.
 */

@Entity
@Data
@Table(name = "operaciones")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Operacion implements Serializable {

    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro_operacion")
    private Long nroOperacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_operacion")
    private Date fechaOperacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_envio")
    private Date fechaEnvio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_entrega")
    private Date fechaEntrega;

    @Enumerated(EnumType.STRING)
    private EstadoOperacion estado;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "cliente_id")
    @NotNull(message = "Datos del cliente obligatorios")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "direccion_envio_id")
    @NotNull(message = "Dirección de envío obligatoria")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Direccion direccionEnvio;

    // Luego crear FormaPago con las distintas formas de pago.
    @NotNull(message = "La forma de pago es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medio_pago_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MedioPago medioPago;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "operacion_pago_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private OperacionPago pago;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "operacion_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<DetalleOperacion> items;

    private Double total;
}
