/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.controller;

import fransis.mpm.model.Articulo;
import fransis.mpm.model.Estado;
import fransis.mpm.model.Item;
import fransis.mpm.repository.ArticuloRepository;
import fransis.mpm.repository.ItemRepository;
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
@Component("itemController")
@RequestMapping("/articulos/{articuloId}")
public class ItemController {

    @Autowired
    private ItemRepository repository;

    @Autowired
    private ArticuloRepository articuloRepository;

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public ResponseEntity<Page<Item>> obtener(Pageable pageRequest, @PathVariable() Long articuloId){
        if(articuloId == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Articulo articulo = articuloRepository.findOne(articuloId);
        if(articulo == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(repository.findByArticulo(articulo, pageRequest));
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET, params = {"estado"})
    public ResponseEntity<Page<Item>> obtener(Pageable pageRequest, @PathVariable() Long articuloId, @RequestParam() Estado estado){
        if(articuloId == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Articulo articulo = articuloRepository.findOne(articuloId);
        if(articulo == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(repository.findByArticuloAndEstado(articulo, estado, pageRequest));
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET, params = {"ordenDeCompra"})
    public ResponseEntity<Page<Item>> obtener(Pageable pageRequest, @PathVariable() Long articuloId, @RequestParam() String ordenDeCompra){

        /*
        if(articuloId == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Articulo articulo = articuloRepository.findOne(articuloId);
        if(articulo == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        */

        return ResponseEntity.ok(repository.findByOrdenDeCompra(ordenDeCompra, pageRequest));
    }

    @RequestMapping(value = "/items", method = RequestMethod.POST)
    public ResponseEntity<Item> agregar(@PathVariable Long articuloId, @RequestBody Item item){
        Articulo a = articuloRepository.findOne(articuloId);
        if(a != null){
            Item i = item;
            i.setArticulo(a);
            i = repository.saveAndFlush(i);
            URI location = null;
            try {
                location = new URI("/articulos/" + a.getArticuloId()+"/items/"+i.getItemId());
            } catch (URISyntaxException e) {
                return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)).body(null);
            }
            return (ResponseEntity.status(HttpStatus.CREATED)).location(location).body(i);
        }else{
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)).body(null);
        }
    }

    @RequestMapping(value = "/items/{itemId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> put(@PathVariable() Long articuloId, @PathVariable() Long itemId, @RequestBody Item item){
        if(item.getItemId() != null && repository.exists(itemId)) {
            Item i = repository.saveAndFlush(item);
            return (ResponseEntity.status(HttpStatus.NO_CONTENT)).build();
        }else{
            return (ResponseEntity.status(HttpStatus.NOT_FOUND)).build();
        }
    }

    @RequestMapping(value = "/items/{itemId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> borrar(@PathVariable Long itemId){

        if(repository.exists(itemId)){
            repository.delete(itemId);
            return ResponseEntity.status(HttpStatus.OK).build();
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

}
