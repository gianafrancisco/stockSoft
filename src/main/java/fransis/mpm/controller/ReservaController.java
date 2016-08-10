/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.controller;

import fransis.mpm.model.Estado;
import fransis.mpm.model.EstadoReserva;
import fransis.mpm.model.Reserva;
import fransis.mpm.repository.ItemRepository;
import fransis.mpm.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static fransis.mpm.model.EstadoReserva.*;

/**
 * Created by francisco on 18/12/15.
 */
@RestController
@Component("reservaController")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ItemRepository itemRepository;

    @RequestMapping(value = "/reservas/{reservaId}", method = RequestMethod.GET)
    public ResponseEntity<Reserva> obtener(@PathVariable Long reservaId){
        Reserva one = reservaRepository.findOne(reservaId);
        if(one != null) {
            return (ResponseEntity.status(HttpStatus.OK)).body(one);
        }else{
            return (ResponseEntity.status(HttpStatus.NOT_FOUND)).body(null);
        }

    }

    @RequestMapping(value = "/reservas", method = RequestMethod.GET)
    public Page<Reserva> obtenerLista(Pageable pageRequest, Principal principal){
        List<EstadoReserva> estadoReservaList = new ArrayList<>();
        estadoReservaList.add(EstadoReserva.ACTIVA);
        estadoReservaList.add(EstadoReserva.CANCELADA);
        estadoReservaList.add(EstadoReserva.CERRADA);
        if(!"Administrador".equals(principal.getName())) {
            return reservaRepository.findByVendedorAndEstadoIn(principal.getName(), estadoReservaList, pageRequest);
        }else{
            return reservaRepository.findAll(pageRequest);
        }
    }

    @RequestMapping(value = "/reservas", method = RequestMethod.GET, params = {"search"})
    public Page<Reserva> filtrarReservas(@RequestParam(value = "") String search, Pageable pageRequest, Principal principal){
        List<EstadoReserva> estadoReservaList = new ArrayList<>();
        estadoReservaList.add(EstadoReserva.ACTIVA);
        estadoReservaList.add(EstadoReserva.CANCELADA);
        estadoReservaList.add(EstadoReserva.CERRADA);
        if(!"Administrador".equals(principal.getName())) {
            return reservaRepository.findByVendedorAndDescripcionContainingIgnoreCaseOrEmailContainingIgnoreCaseAndEstadoIn(principal.getName(), search, search, estadoReservaList, pageRequest);
        }else{
            return reservaRepository.findByDescripcionContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageRequest);
        }
    }

    @RequestMapping(value = "/reservas", method = RequestMethod.POST)
    public ResponseEntity<Reserva> agregar(@RequestBody Reserva reserva, Principal principal){
        Reserva reserva1 = new Reserva(reserva.getDescripcion(), reserva.getEmail(), principal.getName());
        reserva1.setEstado(ACTIVA);
        Reserva r = reservaRepository.saveAndFlush(reserva1);
        URI location = null;
        try {
            location = new URI("/reservas/" + r.getId());
        } catch (URISyntaxException e) {
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)).body(null);
        }
        return (ResponseEntity.status(HttpStatus.CREATED)).location(location).body(r);
    }

    @RequestMapping(value = "/reservas/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> put(@PathVariable() Long id, @RequestBody Reserva reserva){
        if(reserva.getId() != null && reservaRepository.exists(reserva.getId())) {
            Reserva r = reservaRepository.saveAndFlush(reserva);
            switch (r.getEstado()){
                case CANCELADA:
                    itemRepository.findByReserva(r).forEach(item -> {
                        item.setEstado(Estado.DISPONIBLE);
                        item.setReserva(null);
                        itemRepository.saveAndFlush(item);
                    });
                    break;
                case CERRADA:
                    itemRepository.findByReserva(r).forEach(item -> {
                        item.setEstado(Estado.VENDIDO);
                        itemRepository.saveAndFlush(item);
                    });
                    break;
            }
            return (ResponseEntity.status(HttpStatus.NO_CONTENT)).build();
        }else{
            return (ResponseEntity.status(HttpStatus.NOT_FOUND)).build();
        }
    }

    @RequestMapping(value = "/reservas/{reservaId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> borrarArticulo(@PathVariable Long reservaId){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }
    //TODO: Add a new resource in order to get a reserva detail with each item include in the Reserva.
}
