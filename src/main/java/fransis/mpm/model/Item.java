/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by francisco on 6/20/16.
 */
@Entity
@Table(name="item")
public class Item {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", unique = true, nullable = false)
    @Id
    private Long id;
    @ManyToOne
    private Articulo articulo;
    private Estado estado;
    private String ordenDeCompra;
    private Tipo tipo;
    @ManyToOne
    private Reserva reserva;

    private LocalDate fechaRegistro;
    private LocalDate fechaIngreso;

    public Item() {
        this.estado = Estado.DISPONIBLE;
        this.tipo = Tipo.VIRTUAL;
        this.ordenDeCompra = "";
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public Long getId() {
        return id;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setOrdenDeCompra(String ordenDeCompra) {
        this.ordenDeCompra = ordenDeCompra;
    }

    public String getOrdenDeCompra() {
        return ordenDeCompra;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    @JsonIgnore
    public Reserva getReserva() {
        return reserva;
    }
}
