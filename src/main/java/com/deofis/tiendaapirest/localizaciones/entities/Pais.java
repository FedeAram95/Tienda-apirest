package com.deofis.tiendaapirest.localizaciones.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "countries")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pais implements Serializable {

    private final static long serialVersionUID = 1L;

    @Id
    private Long id;
    @Column(name = "name")
    private String nombre;
    private String iso3;
    private String iso2;
    private String phonecode;
    private String capital;
    private String currency;
    @Column(name = "native")
    private String nativo;
    private String region;
    private String subregion;
    private String emoji;
    private String emojiU;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    private Integer flag;
    @Column(name = "wikiDataId")
    private String wikiDataId;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id")
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    private List<Estado> estados;
}
