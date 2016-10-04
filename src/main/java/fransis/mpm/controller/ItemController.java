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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by francisco on 18/12/15.
 */
@RestController
@Component("itemController")
public class ItemController {

    @Autowired
    private ItemRepository repository;

    @Autowired
    private ArticuloRepository articuloRepository;

    @RequestMapping(value = "/articulos/{articuloId}/items", method = RequestMethod.GET)
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

    @RequestMapping(value = "/articulos/{articuloId}/items", method = RequestMethod.GET, params = {"estado"})
    public ResponseEntity<Page<Item>> obtener(Pageable pageRequest, @PathVariable() Long articuloId, @RequestParam() Estado estado){
        if(articuloId == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Articulo articulo = articuloRepository.findOne(articuloId);
        if(articulo == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(repository.findByArticuloAndEstadoOrderByTipoDesc(articulo, estado, pageRequest));
    }

    @RequestMapping(value = "/articulos/{articuloId}/items", method = RequestMethod.GET, params = {"ordenDeCompra"})
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

        return ResponseEntity.ok(repository.findByOrdenDeCompraContainingIgnoreCase(ordenDeCompra, pageRequest));
    }

    @RequestMapping(value = "/articulos/{articuloId}/items", method = RequestMethod.POST)
    public ResponseEntity<Item> agregar(@PathVariable Long articuloId, @RequestBody Item item){
        item.setEstado(Estado.DISPONIBLE);
        Articulo a = articuloRepository.findOne(articuloId);
        if(a != null){
            Item i = item;
            i.setArticulo(a);
            i = repository.saveAndFlush(i);
            URI location = null;
            try {
                location = new URI("/articulos/" + a.getArticuloId()+"/items/"+i.getId());
            } catch (URISyntaxException e) {
                return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)).body(null);
            }
            return (ResponseEntity.status(HttpStatus.CREATED)).location(location).body(i);
        }else{
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)).body(null);
        }
    }

    @RequestMapping(value = "/articulos/{articuloId}/items/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> put(@PathVariable() Long articuloId, @PathVariable() Long id, @RequestBody Item item){
        return updateItem(id, item);
    }

    @RequestMapping(value = "/items/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> put(@PathVariable() Long id, @RequestBody Item item){
        return updateItem(id, item);
    }

    private ResponseEntity<Void> updateItem(@PathVariable() Long id, @RequestBody Item item) {
        if(item.getId() != null && repository.exists(id)) {
            Item i = repository.saveAndFlush(item);
            return (ResponseEntity.status(HttpStatus.NO_CONTENT)).build();
        }else{
            return (ResponseEntity.status(HttpStatus.NOT_FOUND)).build();
        }
    }

    @RequestMapping(value = "/articulos/{articuloId}/items/{itemId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> borrar(@PathVariable Long itemId){

        if(repository.exists(itemId)){
            repository.delete(itemId);
            return ResponseEntity.status(HttpStatus.OK).build();
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @RequestMapping(value = "/items", method = RequestMethod.GET, params = {"ordenDeCompra"})
    public ResponseEntity<Page<Item>> obtener(Pageable pageRequest,  @RequestParam() String ordenDeCompra){
        return obtenerSearch(pageRequest, ordenDeCompra);
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET, params = {"search"})
    public ResponseEntity<Page<Item>> obtenerSearch(Pageable pageRequest,  @RequestParam() String search){
        List<Estado> estados = new ArrayList<>();
        estados.add(Estado.RESERVADO);
        estados.add(Estado.DISPONIBLE);
        return ResponseEntity.ok(repository.search(search, estados, pageRequest));
    }

}
