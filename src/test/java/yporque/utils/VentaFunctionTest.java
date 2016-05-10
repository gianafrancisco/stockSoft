package yporque.utils;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import yporque.model.*;
import yporque.request.VentaRequest;

import java.time.Instant;
import java.util.function.BiFunction;

import static org.hamcrest.Matchers.is;

/**
 * Created by francisco on 24/12/15.
 */
public class VentaFunctionTest {

    @Test
    public void test_venta() throws Exception {

        BiFunction<Instant,VentaRequest,Venta> converter = new VentaFunction();

        Articulo articulo = new Articulo("123456","Articulo 1",20.0,1.0,1.0,10,10);
        VentaRequest ventaRequest = new VentaRequest(articulo,10,new Vendedor("username1","1234","nombre1","apellido1"),"Efectivo", "cupon1", 0.0);

        Instant fecha = Instant.parse("2015-12-13T16:00:00Z");
        String codigoDevolucion = String.format("%x", fecha.getEpochSecond());
        Venta venta = converter.apply(fecha,ventaRequest);

        Assert.assertThat(venta.getCodigo(), Is.is("123456"));
        Assert.assertThat(venta.getDescripcion(), Is.is("Articulo 1"));
        Assert.assertThat(venta.getFecha(), Is.is(fecha));
        Assert.assertThat(venta.getTipoPago(), Is.is(TipoDePago.EFECTIVO));
        Assert.assertThat(venta.getPrecio(), Is.is(20.0));
        Assert.assertThat(venta.getPrecioLista(), Is.is(20.0));
        Assert.assertThat(venta.getCantidad(), Is.is(1));
        Assert.assertThat(venta.getFactor1(), Is.is(1.0));
        Assert.assertThat(venta.getFactor2(), Is.is(1.0));
        Assert.assertThat(venta.getUsername(), Is.is("username1"));
        Assert.assertThat(venta.getNroCupon(), Is.is("cupon1"));
        Assert.assertThat(venta.getCodigoDevolucion(), Is.is(codigoDevolucion));

    }

    @Test
    public void test_venta_set_default_formaPago() throws Exception {

        BiFunction<Instant,VentaRequest,Venta> converter = new VentaFunction();

        Articulo articulo = new Articulo("123456","Articulo 1",20.0,1.0,1.0,10,10);
        VentaRequest ventaRequest = new VentaRequest(articulo,10,new Vendedor("username1","1234","nombre1","apellido1"),"", "cupon1", 0.0);

        Instant fecha = Instant.parse("2015-12-13T16:00:00Z");
        String codigoDevolucion = String.format("%x", fecha.getEpochSecond());

        Venta venta = converter.apply(fecha,ventaRequest);

        Assert.assertThat(venta.getCodigo(), Is.is("123456"));
        Assert.assertThat(venta.getDescripcion(), Is.is("Articulo 1"));
        Assert.assertThat(venta.getFecha(), Is.is(fecha));
        Assert.assertThat(venta.getTipoPago(), Is.is(TipoDePago.TARJETA));
        Assert.assertThat(venta.getPrecio(), Is.is(20.0));
        Assert.assertThat(venta.getPrecioLista(), Is.is(20.0));
        Assert.assertThat(venta.getCantidad(), Is.is(1));
        Assert.assertThat(venta.getFactor1(), Is.is(1.0));
        Assert.assertThat(venta.getFactor2(), Is.is(1.0));
        Assert.assertThat(venta.getUsername(), Is.is("username1"));
        Assert.assertThat(venta.getNroCupon(), Is.is("cupon1"));
        Assert.assertThat(venta.getCodigoDevolucion(), Is.is(codigoDevolucion));

    }


}
