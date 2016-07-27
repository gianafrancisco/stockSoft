/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.controller;

import fransis.mpm.model.Articulo;
import fransis.mpm.model.Estado;
import fransis.mpm.model.Item;
import fransis.mpm.model.Tipo;
import fransis.mpm.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import fransis.mpm.repository.ArticuloRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by francisco on 18/12/15.
 */
@RestController
@Component("articuloController")
public class ArticuloController {

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private ItemRepository itemRepository;

    @RequestMapping(value = "/articulos/{articuloId}", method = RequestMethod.GET)
    public ResponseEntity<Articulo> obtener(@PathVariable Long articuloId){
        Articulo one = articuloRepository.findOne(articuloId);
        if(one != null) {
            return (ResponseEntity.status(HttpStatus.OK)).body(one);
        }else{
            return (ResponseEntity.status(HttpStatus.NOT_FOUND)).body(null);
        }

    }

    @RequestMapping(value = "/articulos", method = RequestMethod.GET)
    public Page<Articulo> obtenerListaArticulos(Pageable pageRequest){
        Page<Articulo> articulos = articuloRepository.findAll(pageRequest);
        articulos.forEach(articulo -> {
            List<Item> items = itemRepository.findByArticuloAndEstado(articulo, Estado.DISPONIBLE);
            long virtual = items.stream().filter(item -> item.getTipo() == Tipo.VIRTUAL).count();
            long fisico = items.stream().filter(item -> item.getTipo() == Tipo.FISICO).count();
            articulo.setStockVirtual(virtual);
            articulo.setStockFisico(fisico);
        });
        return articulos;
    }

    @RequestMapping(value = "/articulos", method = RequestMethod.GET, params = {"search"})
    public Page<Articulo> filtrarArticulos(@RequestParam(value = "") String search, Pageable pageRequest){
        return articuloRepository.findByDescripcionContainingIgnoreCaseOrCodigoContainingIgnoreCase(search, search, pageRequest);
    }


    @RequestMapping(value = "/articulos", method = RequestMethod.POST)
    public ResponseEntity<Articulo> agregar(@RequestBody Articulo articulo){
        Articulo a = articuloRepository.saveAndFlush(articulo);
        URI location = null;
        try {
            location = new URI("/articulos/" + a.getArticuloId());
        } catch (URISyntaxException e) {
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)).body(null);
        }
        return (ResponseEntity.status(HttpStatus.CREATED)).location(location).body(a);
    }

    @RequestMapping(value = "/articulos", method = RequestMethod.PUT)
    public ResponseEntity<Void> put(@RequestBody Articulo articulo){
        if(articulo.getArticuloId() != null && articuloRepository.exists(articulo.getArticuloId())) {
            Articulo a = articuloRepository.saveAndFlush(articulo);
            return (ResponseEntity.status(HttpStatus.NO_CONTENT)).build();
        }else{
            return (ResponseEntity.status(HttpStatus.NOT_FOUND)).build();
        }
    }

    @RequestMapping(value = "/articulos/{articuloId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> borrarArticulo(@PathVariable Long articuloId){

        if(articuloRepository.exists(articuloId)){
            articuloRepository.delete(articuloId);
            return ResponseEntity.status(HttpStatus.OK).build();
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

}
