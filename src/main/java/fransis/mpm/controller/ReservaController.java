/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.controller;

import fransis.mpm.model.Articulo;
import fransis.mpm.model.Reserva;
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

/**
 * Created by francisco on 18/12/15.
 */
@RestController
@Component("reservaController")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

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
    public Page<Reserva> obtenerLista(Pageable pageRequest){
        return reservaRepository.findAll(pageRequest);
    }

    @RequestMapping(value = "/reservas", method = RequestMethod.GET, params = {"search"})
    public Page<Reserva> filtrarReservas(@RequestParam(value = "") String search, Pageable pageRequest){
        return reservaRepository.findAll(pageRequest);
    }


    @RequestMapping(value = "/reservas", method = RequestMethod.POST)
    public ResponseEntity<Reserva> agregar(@RequestBody Reserva reserva){
        Reserva r = reservaRepository.saveAndFlush(reserva);
        URI location = null;
        try {
            location = new URI("/reservas/" + r.getReservaId());
        } catch (URISyntaxException e) {
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)).body(null);
        }
        return (ResponseEntity.status(HttpStatus.CREATED)).location(location).body(r);
    }

    @RequestMapping(value = "/reservas", method = RequestMethod.PUT)
    public ResponseEntity<Void> put(@RequestBody Reserva reserva){
        if(reserva.getReservaId() != null && reservaRepository.exists(reserva.getReservaId())) {
            Reserva r = reservaRepository.saveAndFlush(reserva);
            return (ResponseEntity.status(HttpStatus.NO_CONTENT)).build();
        }else{
            return (ResponseEntity.status(HttpStatus.NOT_FOUND)).build();
        }
    }

    @RequestMapping(value = "/reservas/{reservaId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> borrarArticulo(@PathVariable Long reservaId){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

}
