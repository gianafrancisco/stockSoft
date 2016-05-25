/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.repository;

import fransis.mpm.model.Vendedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by francisco on 12/12/2015.
 */
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    List<Vendedor> findByUsername(String username);
    Page<Vendedor> findByUsernameContainingIgnoreCaseOrNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String username, String nombre, String apellido, Pageable pageable);
}
