package yporque.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import yporque.model.Articulo;

import java.util.Collection;
import java.util.List;

/**
 * Created by francisco on 04/12/2015.
 */
public interface ArticuloRepository extends JpaRepository<Articulo, Long> {
    Page<Articulo> findByDescripcionContainingIgnoreCaseOrCodigoContainingIgnoreCase(String descripcion,String codigo, Pageable pageable);
    List<Articulo> findByCodigo(String codigo);
}
