package yporque.utils;

import org.springframework.stereotype.Component;
import yporque.model.*;
import yporque.request.VentaRequest;

import java.time.Instant;
import java.util.function.BiFunction;

/**
 * Created by francisco on 24/12/15.
 */
@Component("ventaFunction")
public class VentaFunction implements BiFunction<Instant, VentaRequest, Venta> {
    @Override
    public Venta apply(Instant instant, VentaRequest ventaRequest) {
        Articulo articulo = ventaRequest.getArticulo();
        Vendedor vendedor = ventaRequest.getVendedor();
        TipoDePago tipoDePago = getTipoDePago(ventaRequest.getFormaPago());
        return new Venta(instant,
                        articulo.getCodigo(),
                        articulo.getDescripcion(),
                        1,
                        articulo.getFactor1(),
                        articulo.getFactor2(),
                        articulo.getPrecioLista(),
                        articulo.getPrecio()*(1-ventaRequest.getDescuento()/100),
                        tipoDePago,
                        vendedor.getUsername(),
                        ventaRequest.getNroCupon()
        );
    }

    private TipoDePago getTipoDePago(String tipo) {
        switch(tipo){
            case "Efectivo":
                return TipoDePago.EFECTIVO;
            case "Mixto":
                return TipoDePago.MIXTO;
            default:
                return TipoDePago.TARJETA;
        }
    }

}
