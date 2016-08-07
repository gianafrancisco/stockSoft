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

import java.util.List;

/**
 * Created by francisco on 7/2/16.
 */
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    //TODO: Create test in order to test if the vendedor filter is working.
    Page<Reserva> findByVendedorAndDescripcionContainingIgnoreCaseOrEmailContainingIgnoreCaseAndEstadoIn(String vendedor, String descripcion, String email, List<EstadoReserva> estado, Pageable pageable);
    Page<Reserva> findByVendedorAndEstadoIn(String vendedor, List<EstadoReserva> estado, Pageable pageable);
}
