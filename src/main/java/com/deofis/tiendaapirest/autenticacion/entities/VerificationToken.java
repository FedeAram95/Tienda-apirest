package com.deofis.tiendaapirest.autenticacion.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "verificacion_token")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expira_en")
    private Date expiraEn;
}
