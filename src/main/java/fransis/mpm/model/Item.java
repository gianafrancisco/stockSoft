/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.model;

import javax.persistence.*;

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
    @ManyToOne
    private Vendedor vendedor;

    public Item() {
        this.estado = Estado.EN_STOCK;
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

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }
}
