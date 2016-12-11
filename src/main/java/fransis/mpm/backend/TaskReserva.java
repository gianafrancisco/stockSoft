/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.backend;

import fransis.mpm.model.Estado;
import fransis.mpm.model.EstadoReserva;
import fransis.mpm.model.Item;
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
        reservaRepository.findByEstadoIn(EstadoReserva.ACTIVA).stream().filter(
                reserva -> (LocalDateTime.now().minusDays(days).toInstant(ZoneOffset.UTC).toEpochMilli() > reserva.getFechaReserva())
        ).forEach(
                reserva -> {
                    itemRepository.findByReserva(reserva).forEach(item -> {

                        /*
                            Make a copy in order to see a summary when the reserva is cancelada
                        */
                        Item itemSummary = new Item();
                        itemSummary.setReserva(reserva);
                        itemSummary.setTipo(item.getTipo());
                        itemSummary.setEstado(item.getEstado());
                        itemSummary.setArticulo(item.getArticulo());
                        itemSummary.setEstado(Estado.CANCELADO);
                        itemRepository.saveAndFlush(itemSummary);

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
