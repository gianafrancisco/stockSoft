/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.controller;

import fransis.mpm.model.*;
import fransis.mpm.repository.ArticuloRepository;
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
import java.time.Instant;
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

    @Autowired
    private ArticuloRepository articuloRepository;

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
        return reservaRepository.findAll(pageRequest);
    }

    @RequestMapping(value = "/reservas", method = RequestMethod.GET, params = {"search"})
    public Page<Reserva> filtrarReservas(@RequestParam(value = "") String search, Pageable pageRequest, Principal principal){
        List<EstadoReserva> estadoReservaList = new ArrayList<>();
        estadoReservaList.add(EstadoReserva.ACTIVA);
        estadoReservaList.add(EstadoReserva.CANCELADA);
        estadoReservaList.add(EstadoReserva.CERRADA);
        return reservaRepository.findByDescripcionContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageRequest);
    }

    @RequestMapping(value = "/reservas", method = RequestMethod.POST)
    public ResponseEntity<Reserva> agregar(@RequestBody Reserva reserva, Principal principal){
        Reserva reserva1 = new Reserva(reserva.getDescripcion(), reserva.getEmail(), principal.getName(), Instant.now().toEpochMilli());
        reserva1.setEstado(ACTIVA);
        Reserva r = reservaRepository.saveAndFlush(reserva1);
        URI location;
        try {
            location = new URI("/reservas/" + r.getId());
        } catch (URISyntaxException e) {
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)).body(null);
        }
        return (ResponseEntity.status(HttpStatus.CREATED)).location(location).body(r);
    }

    @RequestMapping(value = "/reservas/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> put(@PathVariable() Long id, @RequestBody Reserva reserva, Principal principal){
        if(reserva.getId() != null && reservaRepository.exists(reserva.getId())) {
            if(principal.getName().equals(reserva.getVendedor()) || "Administrador".equals(principal.getName())) {
                switch (reserva.getEstado()) {
                    case CANCELADA:
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
                        break;
                    case CERRADA:
                        itemRepository.findByReserva(reserva).forEach(item -> {
                            item.setEstado(Estado.VENDIDO);
                            itemRepository.saveAndFlush(item);
                        });
                        reserva.setFechaCierre(Instant.now().toEpochMilli());
                        break;
                }
                reservaRepository.saveAndFlush(reserva);
                return (ResponseEntity.status(HttpStatus.NO_CONTENT)).build();
            }else{
                return (ResponseEntity.status(HttpStatus.NOT_MODIFIED)).build();
            }
        }else{
            return (ResponseEntity.status(HttpStatus.NOT_FOUND)).build();
        }
    }

    @RequestMapping(value = "/reservas/{reservaId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> borrarArticulo(@PathVariable Long reservaId){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    @RequestMapping(value = "/reservas/{reservaId}/items", method = RequestMethod.GET)
    public ResponseEntity<List<fransis.mpm.model.web.Item>> obtenerItem(@PathVariable Long reservaId){
        Reserva one = reservaRepository.findOne(reservaId);
        if(one != null) {
            List<Item> items = itemRepository.findByReserva(one);
            final List<fransis.mpm.model.web.Item> itemList = new ArrayList<>();
            items.forEach(item -> itemList.add(new fransis.mpm.model.web.Item(item)));
            return (ResponseEntity.status(HttpStatus.OK)).body(itemList);
        }else{
            return (ResponseEntity.status(HttpStatus.NOT_FOUND)).body(null);
        }

    }

    @RequestMapping(value = "/reservas/articulos", method = RequestMethod.GET, params = {"search"})
    public Page<Articulo> filtrarArticulos(@RequestParam(value = "") String search, Pageable pageRequest){
        Page<Articulo> list = articuloRepository.articulosWithStock("%" + search + "%", pageRequest);
        list.forEach(this::populateStock);
        return list;
    }

    @RequestMapping(value = "/reservas/articulos", method = RequestMethod.GET)
    public Page<Articulo> obtenerListaArticulos(Pageable pageRequest){
        Page<Articulo> articulos = articuloRepository.articulosWithStock("%%",pageRequest);
        articulos.forEach(this::populateStock);
        return articulos;
    }

    private void populateStock(Articulo articulo) {
        List<Item> items = itemRepository.findByArticuloAndEstadoOrderByTipoDesc(articulo, Estado.DISPONIBLE);
        long virtual = items.stream().filter(item -> item.getTipo() == Tipo.VIRTUAL).count();
        long fisico = items.stream().filter(item -> item.getTipo() == Tipo.FISICO).count();
        items = itemRepository.findByArticuloAndEstadoOrderByTipoDesc(articulo, Estado.RESERVADO);
        long fisicoReservados = items.stream().filter(item -> item.getTipo() == Tipo.FISICO).count();
        long virtualReservados = items.stream().filter(item -> item.getTipo() == Tipo.VIRTUAL).count();
        articulo.setStockVirtual(virtual);
        articulo.setStockFisico(fisico);
        articulo.setStockVirtualReservado(virtualReservados);
        articulo.setStockFisicoReservado(fisicoReservados);
    }

}
