package yporque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import yporque.model.*;
import yporque.repository.ArticuloRepository;
import yporque.repository.ResumenRepository;
import yporque.repository.VentaRepository;
import yporque.request.ConfirmarVentaRequest;
import yporque.request.DevolucionRequest;
import yporque.request.VentaRequest;
import yporque.utils.VentaFunction;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;

/**
 * Created by francisco on 23/12/15.
 */
@RestController
@Component("ventaController")
public class VentaController {

    public static final String END_TIME = "2100-05-28T00:00:00Z";
    public static final String START_TIME = "1984-05-28T00:00:00Z";
    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private VentaFunction ventaFunction;

    @Autowired
    private ResumenRepository resumenRepository;

    @RequestMapping("/venta/confirmar")
    public HashMap<String, String> confirmar(@RequestBody ConfirmarVentaRequest params) {

        List<VentaRequest> ventas = params.getArticulos();
        List<DevolucionRequest> devoluciones = params.getDevoluciones();

        Instant fecha = LocalDateTime.now().toInstant(ZoneOffset.UTC);

        ventas.stream().forEach(ventaRequest -> {
            for(int i = 0; i<ventaRequest.getCantidad(); i++) {
                Articulo art = articuloRepository.findOne(ventaRequest.getArticulo().getArticuloId());
                art.setCantidadStock(art.getCantidadStock() - 1);
                articuloRepository.saveAndFlush(art);
                ventaRepository.save(ventaFunction.apply(fecha, ventaRequest));
            }
        });


        devoluciones.stream().forEach(devolucionRequest -> {
            Venta articuloDevuelto = devolucionRequest.getVenta();
            articuloDevuelto.setDevuelto(true);
            ventaRepository.saveAndFlush(articuloDevuelto);
            TipoDePago tipoDePago = getTipoDePago(devolucionRequest.getFormaPago());
            List<Articulo> list = articuloRepository.findByCodigo(articuloDevuelto.getCodigo());
                if(!list.isEmpty()) {
                    Articulo art = list.get(0);
                    art.setCantidadStock(art.getCantidadStock() + 1);
                    articuloRepository.saveAndFlush(art);
                    Venta devolucion = new Venta(fecha,
                            articuloDevuelto.getCodigo(),
                            articuloDevuelto.getDescripcion(),
                            devolucionRequest.getCantidad(),
                            articuloDevuelto.getFactor1(),
                            articuloDevuelto.getFactor2(),
                            articuloDevuelto.getPrecioLista(),
                            -1 * articuloDevuelto.getPrecio(),
                            tipoDePago,
                            devolucionRequest.getVendedor().getUsername(),
                            devolucionRequest.getNroCupon()
                    );

                    ventaRepository.saveAndFlush(devolucion);
                }
            }
        );

        Resumen resumen = new Resumen(fecha,getTipoDePago(params.getFormaPago()),params.getEfectivo(),params.getTarjeta());
        resumenRepository.saveAndFlush(resumen);

        HashMap<String,String> map = new HashMap<>();
        map.put("codigoDevolucion",String.format("%x", fecha.getEpochSecond()));
        return map;

    }

    private TipoDePago getTipoDePago(String tipo) {
        switch(tipo){
            case "Efectivo":
                return TipoDePago.EFECTIVO;
            case "Tajeta":
                return TipoDePago.TARJETA;
            default:
                return TipoDePago.MIXTO;
        }
    }


    @RequestMapping("/ventas")
    public Page<Venta> obtenerListado(@RequestParam(required = false) Instant startTime, @RequestParam(required = false) Instant endTime, Pageable pageRequest) {

        Instant end = Instant.parse(END_TIME);
        Instant start = Instant.parse(START_TIME);
        if(endTime != null){
            end = endTime;
        }
        if(startTime != null){
            start = startTime;
        }

        return ventaRepository.findByFechaBetween(start, end, pageRequest);
    }

    @RequestMapping("/venta/search")
    public Page<Venta> filtrar(@RequestParam(value = "") String search,@RequestParam(required = false) Instant startTime, @RequestParam(required = false) Instant endTime, Pageable pageRequest){

        Instant end = Instant.parse(END_TIME);
        Instant start = Instant.parse(START_TIME);
        if(endTime != null){
            end = endTime;
        }
        if(startTime != null){
            start = startTime;
        }

        return ventaRepository.filtrar(start, end, search, pageRequest);
    }


    @RequestMapping("/venta/devolucion")
    public List<Venta> filtrarPorCodigoDevolucion(@RequestParam(required = true) String codigoDevolucion){
        return ventaRepository.findByCodigoDevolucion(codigoDevolucion);
    }


}

