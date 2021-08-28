package com.deofis.tiendaapirest.localizaciones.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "cities")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ciudad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    @Column(name = "name")
    private String nombre;
    @Column(name = "state_code")
    private String stateCode;
    @Column(name = "country_id")
    private Long countryId;
    @Column(name = "country_code")
    private String countryCode;
    private Double latitude;
    private Double longitude;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;
    private Integer flag;
    @Column(name = "wikiDataId")
    private String wikiDataId;
}
