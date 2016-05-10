package yporque.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yporque.model.Vendedor;

import java.util.List;

/**
 * Created by francisco on 12/12/2015.
 */
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    List<Vendedor> findByUsername(String username);
    Page<Vendedor> findByUsernameContainingIgnoreCaseOrNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String username, String nombre, String apellido, Pageable pageable);
}
