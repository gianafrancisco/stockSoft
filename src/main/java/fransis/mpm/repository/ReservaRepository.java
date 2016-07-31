/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.repository;

import fransis.mpm.model.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by francisco on 7/2/16.
 */
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    Page<Reserva> findByDescripcionContainingIgnoreCaseOrEmailContainingIgnoreCase(String descripcion, String email, Pageable pageable);
}
