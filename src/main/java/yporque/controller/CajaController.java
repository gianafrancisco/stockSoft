package yporque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yporque.model.*;
import yporque.repository.CajaRepository;
import yporque.repository.ResumenRepository;
import yporque.repository.RetiroRepository;
import yporque.repository.VentaRepository;
import yporque.request.CajaRequest;

import java.time.*;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Created by francisco on 23/12/15.
 */
@RestController
@Component("cajaController")
public class CajaController {

    @Autowired
    private CajaRepository cajaRepository;

    @Autowired
    private ResumenRepository resumenRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private RetiroRepository retiroRepository;

    @Autowired
    private BiFunction<Instant, String, Caja> cierreFunction;

    @RequestMapping("/caja/abrir")
    public Caja abrir(@RequestBody CajaRequest cajaRequest){

        List<Caja> cajaList = cajaRepository.findAll(new Sort(Sort.Direction.DESC,"cierre"));
        cajaRepository.saveAndFlush(new Caja(cajaRequest.getFecha(), cajaRequest.getUsername()));
        if(!cajaList.isEmpty()) {
            Double monto = cajaList.get(0).getEfectivoDiaSiguiente();
            Instant fecha = LocalDateTime.now().toInstant(ZoneOffset.UTC);;
            Venta venta = new Venta(fecha,
                        "CAJA",
                        "Apertura de caja: Efectivo disponible",
                        1,
                        1.0,
                        1.0,
                        monto,
                        monto,
                        TipoDePago.EFECTIVO,
                        cajaRequest.getUsername(),
                        ""
                    );
            Resumen resumen = new Resumen(fecha,TipoDePago.EFECTIVO,monto,0.0);
            resumenRepository.saveAndFlush(resumen);

            ventaRepository.save(venta);
        }
        return cierreFunction.apply(cajaRequest.getFecha(), cajaRequest.getUsername());
    }

    @RequestMapping("/caja/cerrar")
    public Caja cerrar(@RequestBody CajaRequest cajaRequest) {
        return cajaRepository.save(cierreFunction.apply(cajaRequest.getFecha(),cajaRequest.getUsername()));
    }

    @RequestMapping("/caja/abierta")
    public Boolean isOpen() {
        return !cajaRepository.findByCierre(Instant.EPOCH).isEmpty();
    }

    @RequestMapping("/caja/resumen")
    public Caja resumen(@RequestBody CajaRequest cajaRequest) {
        return cierreFunction.apply(cajaRequest.getFecha(), cajaRequest.getUsername());
    }

    @RequestMapping("/caja/imprimir/{id}")
    public ResumenCaja imprimir(@PathVariable Long id) {
        Caja caja = cajaRepository.findOne(id);
        List<Venta> ventaList = ventaRepository.findByFechaBetween(caja.getApertura(),caja.getCierre());
        List<Retiro>retiroList = retiroRepository.findByFechaBetween(caja.getApertura(),caja.getCierre());

        return new ResumenCaja(caja,ventaList,retiroList);

    }

}

