/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.controller;

import fransis.mpm.model.Articulo;
import fransis.mpm.model.Estado;
import fransis.mpm.model.Item;
import fransis.mpm.model.Tipo;
import fransis.mpm.function.ExportarService;
import fransis.mpm.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import fransis.mpm.repository.ArticuloRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
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
    private ExportarService exportarService;

    @Autowired
    private ItemRepository itemRepository;

    @RequestMapping(value = "/articulos/{articuloId}", method = RequestMethod.GET)
    public ResponseEntity<Articulo> obtener(@PathVariable Long articuloId){
        Articulo one = articuloRepository.findOne(articuloId);
        if(one != null) {
            populateStock(one);
            return (ResponseEntity.status(HttpStatus.OK)).body(one);
        }else{
            return (ResponseEntity.status(HttpStatus.NOT_FOUND)).body(null);
        }

    }

    @RequestMapping(value = "/articulos", method = RequestMethod.GET)
    public Page<Articulo> obtenerListaArticulos(Pageable pageRequest){
        Page<Articulo> articulos = articuloRepository.findAll(pageRequest);
        articulos.forEach(articulo -> {
            populateStock(articulo);
        });
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

    @RequestMapping(value = "/articulos", method = RequestMethod.GET, params = {"search"})
    public Page<Articulo> filtrarArticulos(@RequestParam(value = "") String search, Pageable pageRequest){
        Page<Articulo> list = articuloRepository.findByDescripcionContainingIgnoreCaseOrCodigoContainingIgnoreCase(search, search, pageRequest);
        list.forEach(articulo -> populateStock(articulo));
        return list;
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
        populateStock(a);
        return (ResponseEntity.status(HttpStatus.CREATED)).location(location).body(a);
    }

    @RequestMapping(value = "/articulos", method = RequestMethod.PUT)
    public ResponseEntity<Void> put(@RequestBody Articulo articulo){
        if(articulo.getArticuloId() != null && articuloRepository.exists(articulo.getArticuloId())) {
            Articulo a = articuloRepository.findOne(articulo.getArticuloId());
            if(a.getPrecioDeInventario() != articulo.getPrecioDeInventario()){
                articulo.setFechaActualizacion(Instant.now().toEpochMilli());
            }
            articuloRepository.saveAndFlush(articulo);
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

    @RequestMapping(value = "/articulos/export", method = RequestMethod.GET)
    public void exportar(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition","attachment;filename=articulos.xls");
        exportarService.exportar(response.getOutputStream());
        response.flushBuffer();
    }

}
