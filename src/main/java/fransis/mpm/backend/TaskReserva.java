package fransis.mpm.backend;

import fransis.mpm.model.Estado;
import fransis.mpm.model.EstadoReserva;
import fransis.mpm.repository.ItemRepository;
import fransis.mpm.repository.ReservaRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Created by francisco on 19/08/2016.
 */

public class TaskReserva {

    public static void cerrarReserva(ReservaRepository reservaRepository, ItemRepository itemRepository, int days){
        reservaRepository.findByEstadoIn(EstadoReserva.ACTIVA).stream().filter(reserva -> {
            return (LocalDateTime.now().minusDays(days).toInstant(ZoneOffset.UTC).toEpochMilli() > reserva.getFechaReserva());
        }).forEach(
                reserva -> {
                    itemRepository.findByReserva(reserva).forEach(item -> {
                        item.setEstado(Estado.DISPONIBLE);
                        item.setReserva(null);
                        itemRepository.saveAndFlush(item);
                    });
                    reserva.setFechaCierre(Instant.now().toEpochMilli());
                    reserva.setEstado(EstadoReserva.CANCELADA);
                    reservaRepository.save(reserva);
                }
        );
    }

}
