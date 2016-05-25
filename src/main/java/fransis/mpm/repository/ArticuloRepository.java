/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.repository;

import fransis.mpm.model.Articulo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by francisco on 04/12/2015.
 */
public interface ArticuloRepository extends JpaRepository<Articulo, Long> {
    Page<Articulo> findByDescripcionContainingIgnoreCaseOrCodigoContainingIgnoreCase(String descripcion, String codigo, Pageable pageable);
    List<Articulo> findByCodigo(String codigo);
}
