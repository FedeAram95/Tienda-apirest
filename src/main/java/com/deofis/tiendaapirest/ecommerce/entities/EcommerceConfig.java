package com.deofis.tiendaapirest.ecommerce.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "config")
@AllArgsConstructor
@NoArgsConstructor
public class EcommerceConfig implements Serializable {

    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logo;

    private String colorNav;

    private String colorFondo;

}
