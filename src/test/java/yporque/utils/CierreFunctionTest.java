package yporque.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yporque.config.MemoryDBConfig;
import yporque.model.*;
import yporque.repository.CajaRepository;
import yporque.repository.ResumenRepository;
import yporque.repository.RetiroRepository;
import yporque.repository.VentaRepository;
import yporque.request.VentaRequest;

import java.time.Instant;
import java.util.function.BiFunction;

import static org.hamcrest.Matchers.is;

/**
 * Created by francisco on 24/12/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("yporque.model")
@ContextConfiguration(classes = {MemoryDBConfig.class, CierreFunction.class})
public class CierreFunctionTest {

    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private CajaRepository cajaRepository;
    @Autowired
    private RetiroRepository retiroRepository;
    @Autowired
    private CierreFunction cierreFunction;
    @Autowired
    private ResumenRepository resumenRepository;

    private BiFunction<Instant, VentaRequest, Venta> converter;

    @Before
    public void setUp() throws Exception {
        //converter = new VentaFunction();
        //Articulo articulo = new Articulo("123456", "Articulo 1", 200.0, 1.0, 2.0, 20, 20);

        for (int i = 0;i < 10; i++){
            Resumen resumen = new Resumen(Instant.parse("2015-12-30T16:00:00Z"),TipoDePago.EFECTIVO,400.0,0.0);
            resumenRepository.saveAndFlush(resumen);
        }

        for (int i = 0;i < 5; i++){
            Resumen resumen = new Resumen(Instant.parse("2015-12-30T16:00:00Z"),TipoDePago.TARJETA,0.0,400.0);
            resumenRepository.saveAndFlush(resumen);
        }

        /*
        for(int i = 0;i < 10; i++) {
            VentaRequest ventaRequest = new VentaRequest(articulo, 10, new Vendedor("username1", "1234", "nombre1", "apellido1"), "Efectivo", "cupon1");
            Venta venta = converter.apply(Instant.parse("2015-12-30T16:00:00Z"), ventaRequest);
            ventaRepository.save(venta);
        }
        for(int i = 0; i < 5; i++) {
            VentaRequest ventaRequest1 = new VentaRequest(articulo, 5, new Vendedor("username1", "1234", "nombre1", "apellido1"), "Tarjeta", "cupon2");
            Venta venta1 = converter.apply(Instant.parse("2015-12-30T16:10:00Z"), ventaRequest1);
            ventaRepository.save(venta1);
        }
        */


        retiroRepository.save(new Retiro(1000.0, "Retiro efectivo del dia", Instant.parse("2015-12-30T12:00:00Z"), "username1"));
        retiroRepository.save(new Retiro(50.0, "taxi", Instant.parse("2015-12-30T12:10:00Z"), "username1"));

        Caja caja = new Caja(Instant.parse("2015-12-30T09:00:00Z"), "username1");
        cajaRepository.save(caja);

    }

    @After
    public void tearDown() throws Exception {
        ventaRepository.deleteAll();
        cajaRepository.deleteAll();
        retiroRepository.deleteAll();
    }

    @Test
    public void test_cierre_de_caja() throws Exception {
        Caja caja = cierreFunction.apply(Instant.parse("2015-12-30T21:00:00Z"), "username2");

        Assert.assertThat(caja.getEfectivo(), is(4000.0));
        Assert.assertThat(caja.getTarjeta(), is(2000.0));
        Assert.assertThat(caja.getTotalVentaDia(), is(4000.0 + 2000.0));
        Assert.assertThat(caja.getEfectivoDiaSiguiente(), is(4000.0 - 1050.0));
        Assert.assertThat(caja.getCierreUsername(), is("username2"));
        Assert.assertThat(caja.getCierre(), is(Instant.parse("2015-12-30T21:00:00Z")));

    }

    @Test(expected = RuntimeException.class)
    public void test_cierre_de_caja_not_found() throws Exception {

        cajaRepository.deleteAll();
        Caja caja = cierreFunction.apply(Instant.parse("2015-12-30T21:00:00Z"), "username2");

    }

}
