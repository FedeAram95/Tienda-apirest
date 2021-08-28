package com.deofis.tiendaapirest.perfiles.entities;

import com.deofis.tiendaapirest.autenticacion.entities.Usuario;
import com.deofis.tiendaapirest.carrito.entities.Carrito;
import com.deofis.tiendaapirest.clientes.entities.Cliente;
import com.deofis.tiendaapirest.favoritos.entities.Favorito;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "perfiles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Perfil implements Serializable {

    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_id", unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente cliente;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "favoritos_id")
    private Favorito favorito;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_id")
    private List<Operacion> compras;
}
