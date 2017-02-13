/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.repository;

import fransis.mpm.model.Articulo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by francisco on 04/12/2015.
 */
public interface ArticuloRepository extends JpaRepository<Articulo, Long> {
    Page<Articulo> findByDescripcionContainingIgnoreCaseOrCodigoContainingIgnoreCase(String descripcion, String codigo, Pageable pageable);
    List<Articulo> findByCodigo(String codigo);
    //select a.* FROM articulo a, item i where a.articulo_id = i.articulo_articulo_id and i.estado < 2;
    @Query(value = "SELECT a FROM Articulo a, Item i WHERE ( UPPER(a.descripcion) LIKE UPPER(?1) OR UPPER(a.codigo) LIKE UPPER(?1) ) AND  a.articuloId = i.articulo.articuloId AND i.estado < 2",
            countQuery = "SELECT count(a) FROM Articulo a, Item i WHERE ( UPPER(a.descripcion) LIKE UPPER(?1) OR UPPER(a.codigo) LIKE UPPER(?1) ) AND  a.articuloId = i.articulo.articuloId AND i.estado < 2")
    Page<Articulo> articulosWithStock(String search, Pageable pageable);
}
