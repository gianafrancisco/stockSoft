/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.model;

import javax.persistence.*;
import java.time.Instant;

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
    private String codigo;
    @Transient
    private Long stockFisico;
    @Transient
    private Long stockVirtual;
    @Transient
    private Long stockVirtualReservado;
    @Transient
    private Long stockFisicoReservado;

    private Long fechaActualizacion;
    private Double precioDeInventario;
    private Moneda moneda;

    public Articulo() {
        this.descripcion = "";
        this.codigo = "";
        this.stockFisico = 0L;
        this.stockVirtual = 0L;
        this.fechaActualizacion = Instant.now().toEpochMilli();
        this.precioDeInventario = 0.0;
        this.moneda = Moneda.DOLAR;
    }

    public Articulo(String codigo, String descripcion) {
        this();
        this.codigo = codigo;
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

    public Long getStockFisico() {
        this.stockFisico = this.stockFisico == null ? 0 : this.stockFisico;
        return stockFisico;
    }

    public Long getStockVirtual() {
        this.stockVirtual = this.stockVirtual == null ? 0 : this.stockVirtual;
        return stockVirtual;
    }

    public void setStockFisico(Long stockFisico) {
        this.stockFisico = stockFisico;
    }

    public void setStockVirtual(Long stockVirtual) {
        this.stockVirtual = stockVirtual;
    }

    public void setStockVirtualReservado(long stockVirtualReservado) {
        this.stockVirtualReservado = stockVirtualReservado;
    }

    public Long getStockVirtualReservado() {
        this.stockVirtualReservado = this.stockVirtualReservado == null ? 0 : this.stockVirtualReservado;
        return stockVirtualReservado;
    }

    public void setStockFisicoReservado(long stockFisicoReservado) {
        this.stockFisicoReservado = stockFisicoReservado;
    }

    public Long getStockFisicoReservado() {
        this.stockFisicoReservado = this.stockFisicoReservado == null ? 0 : this.stockFisicoReservado;
        return stockFisicoReservado;
    }

    public Double getPrecioDeInventario() {
        this.precioDeInventario = this.precioDeInventario == null ? 0.0 : this.precioDeInventario;
        return precioDeInventario;
    }

    public void setPrecioDeInventario(Double precioDeInventario) {
        this.precioDeInventario = precioDeInventario;
    }

    public Long getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Long fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }
}

