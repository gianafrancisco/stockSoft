package yporque.controller;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yporque.config.MemoryDBConfig;
import yporque.model.*;
import yporque.repository.CajaRepository;
import yporque.repository.ResumenRepository;
import yporque.repository.RetiroRepository;
import yporque.repository.VentaRepository;
import yporque.request.CajaRequest;
import yporque.request.VentaRequest;
import yporque.utils.CierreFunction;
import yporque.utils.VentaFunction;

import java.time.Instant;
import java.util.List;
import java.util.function.BiFunction;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

/**
 * Created by francisco on 18/12/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("yporque")
@ContextConfiguration(classes = {MemoryDBConfig.class, CajaController.class, CierreFunction.class})

public class CajaControllerTest {

    private MockMvc mockMvc;

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

    @Autowired
    private CajaController cajaController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(cajaController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
    }

    @After
    public void tearDown() throws Exception {
        ventaRepository.deleteAll();
        cajaRepository.deleteAll();
        retiroRepository.deleteAll();
        resumenRepository.deleteAll();
    }

    @Test
    public void test_abrir_caja() throws Exception {

        Caja caja = cajaController.abrir(new CajaRequest("username1", Instant.parse("2015-12-30T09:00:00Z")));

        List<Caja> cajaList = cajaRepository.findByCierre(Instant.EPOCH);

        Assert.assertThat(cajaList, hasSize(1));
        Assert.assertThat(cajaList.get(0).getAperturaUsername(), is("username1"));

    }

    @Test
    public void test_cerrar_caja() throws Exception {

        Resumen resumen = new Resumen(Instant.parse("2015-12-30T16:00:00Z"),TipoDePago.EFECTIVO,200.0,0.0);
        resumenRepository.save(resumen);
        resumen = new Resumen(Instant.parse("2015-12-30T16:10:00Z"),TipoDePago.EFECTIVO,100.0,0.0);
        resumenRepository.save(resumen);
        resumen = new Resumen(Instant.parse("2015-12-30T16:20:00Z"),TipoDePago.TARJETA,0.0,200.0);
        resumenRepository.save(resumen);
        resumen = new Resumen(Instant.parse("2015-12-30T16:30:00Z"),TipoDePago.TARJETA,0.0,100.0);
        resumenRepository.save(resumen);
        resumen = new Resumen(Instant.parse("2015-12-30T16:30:00Z"),TipoDePago.MIXTO,100.0,100.0);
        resumenRepository.save(resumen);

        retiroRepository.save(new Retiro(100.0, "Retiro efectivo del dia", Instant.parse("2015-12-30T12:00:00Z"), "username1"));
        retiroRepository.save(new Retiro(50.0, "taxi", Instant.parse("2015-12-30T12:10:00Z"), "username1"));

        Caja caja = new Caja(Instant.parse("2015-12-30T09:00:00Z"), "username1");
        cajaRepository.save(caja);


        Caja caja1 = cajaController.cerrar(new CajaRequest("username2", Instant.parse("2015-12-30T21:00:00Z")));

        Boolean isOpen = cajaController.isOpen();

        Assert.assertThat(caja1.getEfectivo(), Matchers.is(400.0));
        Assert.assertThat(caja1.getTarjeta(), Matchers.is(400.0));
        Assert.assertThat(caja1.getTotalVentaDia(), Matchers.is(400.0 + 400.0));
        Assert.assertThat(caja1.getEfectivoDiaSiguiente(), Matchers.is(400.0 - 150.0));
        Assert.assertThat(caja1.getCierreUsername(), Matchers.is("username2"));
        Assert.assertThat(caja1.getCierre(), Matchers.is(Instant.parse("2015-12-30T21:00:00Z")));
        Assert.assertThat(isOpen,is(false));

    }

    @Test
    public void test_resumen_caja() throws Exception {

        Resumen resumen = new Resumen(Instant.parse("2015-12-30T16:00:00Z"),TipoDePago.EFECTIVO,200.0,0.0);
        resumenRepository.save(resumen);
        resumen = new Resumen(Instant.parse("2015-12-30T16:10:00Z"),TipoDePago.EFECTIVO,100.0,0.0);
        resumenRepository.save(resumen);
        resumen = new Resumen(Instant.parse("2015-12-30T16:20:00Z"),TipoDePago.TARJETA,0.0,200.0);
        resumenRepository.save(resumen);
        resumen = new Resumen(Instant.parse("2015-12-30T16:30:00Z"),TipoDePago.TARJETA,0.0,100.0);
        resumenRepository.save(resumen);
        resumen = new Resumen(Instant.parse("2015-12-30T16:30:00Z"),TipoDePago.MIXTO,100.0,100.0);
        resumenRepository.save(resumen);


        retiroRepository.save(new Retiro(100.0, "Retiro efectivo del dia", Instant.parse("2015-12-30T12:00:00Z"), "username1"));
        retiroRepository.save(new Retiro(50.0, "taxi", Instant.parse("2015-12-30T12:10:00Z"), "username1"));

        Caja caja = new Caja(Instant.parse("2015-12-30T09:00:00Z"), "username1");
        cajaRepository.save(caja);

        Caja caja1 = cajaController.resumen(new CajaRequest("username2", Instant.parse("2015-12-30T21:00:00Z")));

        Boolean isOpen = cajaController.isOpen();

        Assert.assertThat(caja1.getEfectivo(), Matchers.is(400.0));
        Assert.assertThat(caja1.getTarjeta(), Matchers.is(400.0));
        Assert.assertThat(caja1.getTotalVentaDia(), Matchers.is(400.0 + 400.0));
        Assert.assertThat(caja1.getEfectivoDiaSiguiente(), Matchers.is(400.0 - 150.0));
        Assert.assertThat(caja1.getCierreUsername(), Matchers.is("username2"));
        Assert.assertThat(caja1.getCierre(), Matchers.is(Instant.parse("2015-12-30T21:00:00Z")));
        Assert.assertThat(isOpen,is(true));

    }

    @Test
    public void test_hay_caja_abierta_true() throws Exception {

        Caja caja = cajaController.abrir(new CajaRequest("username1", Instant.parse("2015-12-30T09:00:00Z")));
        Boolean isOpen = cajaController.isOpen();
        Assert.assertThat(isOpen,is(true));

    }

    @Test
    public void test_hay_caja_abierta_false() throws Exception {

        cajaController.abrir(new CajaRequest("username1", Instant.parse("2015-12-30T09:00:00Z")));
        cajaController.cerrar(new CajaRequest("username1", Instant.parse("2015-12-30T21:00:00Z")));
        Boolean isOpen = cajaController.isOpen();
        Assert.assertThat(isOpen,is(false));

    }

    @Test
    public void test_imprimir_caja() throws Exception {

        converter = new VentaFunction();
        Articulo articulo = new Articulo("123456", "Articulo 1", 200.0, 1.0, 2.0, 20, 20);
        VentaRequest ventaRequest = new VentaRequest(articulo, 10, new Vendedor("username1", "1234", "nombre1", "apellido1"), "Efectivo", "cupon1", 0.0);
        VentaRequest ventaRequest1 = new VentaRequest(articulo, 5, new Vendedor("username1", "1234", "nombre1", "apellido1"), "Tarjeta", "cupon2", 0.0);
        Venta venta = converter.apply(Instant.parse("2015-12-30T16:00:00Z"), ventaRequest);
        Venta venta1 = converter.apply(Instant.parse("2015-12-30T16:10:00Z"), ventaRequest1);
        ventaRepository.save(venta);
        ventaRepository.save(venta1);

        retiroRepository.save(new Retiro(1000.0, "Retiro efectivo del dia", Instant.parse("2015-12-30T12:00:00Z"), "username1"));
        retiroRepository.save(new Retiro(50.0, "taxi", Instant.parse("2015-12-30T12:10:00Z"), "username1"));

        Caja caja = new Caja(Instant.parse("2015-12-30T09:00:00Z"), "username1");
        cajaRepository.save(caja);

        Caja caja1 = cajaController.cerrar(new CajaRequest("username2", Instant.parse("2015-12-30T21:00:00Z")));

        ResumenCaja resumenCaja = cajaController.imprimir(caja1.getCajaId());

        Assert.assertThat(resumenCaja.getCaja().getApertura(),is(Instant.parse("2015-12-30T09:00:00Z")));
        Assert.assertThat(resumenCaja.getCaja().getCierre(),is(Instant.parse("2015-12-30T21:00:00Z")));
        Assert.assertThat(resumenCaja.getVentaList(),hasSize(2));
        Assert.assertThat(resumenCaja.getRetiroList(),hasSize(2));

    }

}
