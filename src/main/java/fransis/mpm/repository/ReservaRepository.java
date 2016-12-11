/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.repository;

import fransis.mpm.model.EstadoReserva;
import fransis.mpm.model.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by francisco on 7/2/16.
 */
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query(value = "SELECT r FROM Reserva r WHERE r.vendedor = ?1 AND (r.descripcion LIKE %?2% OR r.email LIKE %?3%) AND estado IN ?4",
            countQuery = "SELECT count(r) FROM Reserva r WHERE r.vendedor = ?1 AND (r.descripcion LIKE %?2% OR r.email LIKE %?3%) AND estado IN ?4",
            nativeQuery = false)
    Page<Reserva> findByVendedorAndDescripcionContainingIgnoreCaseOrEmailContainingIgnoreCaseAndEstadoIn(String vendedor, String descripcion, String email, List<EstadoReserva> estado, Pageable pageable);
    Page<Reserva> findByDescripcionContainingIgnoreCaseOrEmailContainingIgnoreCase(String descripcion, String email, Pageable pageable);
    Page<Reserva> findByVendedorAndEstadoIn(String vendedor, List<EstadoReserva> estado, Pageable pageable);
    List<Reserva> findByEstadoIn(EstadoReserva estadoReserva);
}
