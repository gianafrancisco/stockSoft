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
    private String descripcion;
    private final String codigo;
    @Transient
    private Long stockFisico;
    @Transient
    private Long stockVirtual;

    public Articulo() {
        this.descripcion = "";
        this.codigo = "";
        this.stockFisico = 0L;
        this.stockVirtual = 0L;
    }

    public Articulo(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.stockFisico = 0L;
        this.stockVirtual = 0L;
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

    public Long getStockFisico() {
        return stockFisico;
    }

    public Long getStockVirtual() {
        return stockVirtual;
    }

    public void setStockFisico(Long stockFisico) {
        this.stockFisico = stockFisico;
    }

    public void setStockVirtual(Long stockVirtual) {
        this.stockVirtual = stockVirtual;
    }
}

