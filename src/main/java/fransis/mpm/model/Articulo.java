/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.model;

import javax.persistence.*;

/**
 * Created by francisco on 21/11/15.
 */
@Entity
@Table(name="articulo")
public class Articulo {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "articulo_id", unique = true, nullable = false)
    @Id
    private Long articuloId;
    @Column(name = "precio_lista")
    private final Double precioLista;
    private String descripcion;
    private final String codigo;


    public Articulo() {
        this.precioLista = 0.0;
        this.descripcion = "";
        this.codigo = "";
    }

    public Articulo(String codigo, String descripcion, Double precioLista) {
        this.codigo = codigo;
        this.precioLista = precioLista;
        this.descripcion = descripcion;
    }

    public Long getArticuloId() {
        return articuloId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public Double getPrecioLista() {
        return precioLista;
    }

}

