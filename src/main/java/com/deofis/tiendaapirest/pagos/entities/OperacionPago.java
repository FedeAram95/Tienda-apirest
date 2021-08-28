package com.deofis.tiendaapirest.pagos.entities;

import com.deofis.tiendaapirest.imagenes.entities.Imagen;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "operacion_pagos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperacionPago implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private Long nroOperacion;

    private String status;

    private Date expiraEn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_pagado")
    private Date fechaPagado;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "comprobante_id")
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
    private Imagen comprobante;

    private String approveUrl;

    private String totalBruto;

    private String totalNeto;

    private String fee;

    private String payerId;

    private String payerEmail;

    private String payerFullName;

    public boolean isExpired() {
        if (this.expiraEn == null)
            return false;

        Date currentDate = Calendar.getInstance().getTime();
        return currentDate.after(this.getExpiraEn());
    }
}
