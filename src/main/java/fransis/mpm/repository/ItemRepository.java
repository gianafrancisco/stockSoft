/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.repository;

import fransis.mpm.model.Articulo;
import fransis.mpm.model.Estado;
import fransis.mpm.model.Item;
import fransis.mpm.model.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * Created by francisco on 6/20/16.
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByArticulo(Articulo articulo, Pageable pageable);
    Page<Item> findByArticuloAndEstado(Articulo articulo, Estado estado, Pageable pageable);
    List<Item> findByArticuloAndEstado(Articulo articulo, Estado estado);
    List<Item> findByReserva(Reserva reserva);
    Page<Item> findByOrdenDeCompraContainingIgnoreCase(String ordenDeCompra, Pageable pageable);
    @Query(value = "SELECT i FROM Item i WHERE ( ordenDeCompra LIKE %?1% OR i.articulo.codigo LIKE %?1% OR i.articulo.descripcion LIKE %?1% ) AND i.estado IN ?2 ",
            countQuery = "SELECT count(i) FROM Item i WHERE ( ordenDeCompra LIKE %?1% OR i.articulo.codigo LIKE %?1% OR i.articulo.descripcion LIKE %?1% ) AND i.estado IN ?2 ")
    Page<Item> search(String search, List<Estado> estado, Pageable pageable);

}
