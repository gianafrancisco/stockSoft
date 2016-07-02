/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.model;

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
    private Long itemId;
    @ManyToOne
    private Articulo articulo;
    private Estado estado;
    private String ordenCompra;
    private Tipo tipo;
    @ManyToOne
    private Reserva reserva;

    private LocalDate fechaRegistro;
    private LocalDate fechaIngreso;

    public Item() {
        this.estado = Estado.DISPONIBLE;
        this.tipo = Tipo.VIRTUAL;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public Long getItemId() {
        return itemId;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

}
