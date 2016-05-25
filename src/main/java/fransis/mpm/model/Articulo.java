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
    private final Double factor1;
    private final Double factor2;
    private String descripcion;
    private final String codigo;


    public Articulo() {
        this.precioLista = 0.0;
        this.factor1 = 1.0;
        this.factor2 = 1.0;
        this.descripcion = "";
        this.codigo = "";
    }

    public Articulo(String codigo, String descripcion, Double precioLista, Double factor1, Double factor2) {
        this.codigo = codigo;
        this.precioLista = precioLista;
        this.factor1 = factor1;
        this.factor2 = factor2;
        this.descripcion = descripcion;
    }

    public Long getArticuloId() {
        return articuloId;
    }

    public Double getPrecio() {
        return actualizarPrecio();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    private Double actualizarPrecio(){
        return Math.ceil(precioLista*factor1*factor2*10)/10;
    }

    public String getCodigo() {
        return codigo;
    }

    public Double getPrecioLista() {
        return precioLista;
    }

    public Double getFactor1() {
        return factor1;
    }

    public Double getFactor2() {
        return factor2;
    }

}

