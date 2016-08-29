/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.model.web;

import fransis.mpm.model.Articulo;
import fransis.mpm.model.Estado;
import fransis.mpm.model.Tipo;

/**
 * Created by francisco on 8/29/16.
 */
public class Item {

    private fransis.mpm.model.Item item;

    public Item(fransis.mpm.model.Item item){
        this.item = item;
    }

    public Articulo getArticulo(){
        return this.item.getArticulo();
    }

    public Tipo getTipo(){
        return this.item.getTipo();
    }

    public String getOrdenDeCompra(){
        return this.item.getOrdenDeCompra();
    }

    public Estado getEstado(){
        return this.item.getEstado();
    }

    public Long getId(){
        return this.item.getId();
    }
}
