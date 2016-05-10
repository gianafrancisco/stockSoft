package yporque.controller;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yporque.config.MemoryDBConfig;
import yporque.model.*;
import yporque.repository.ArticuloRepository;
import yporque.repository.ResumenRepository;
import yporque.repository.VentaRepository;
import yporque.request.ConfirmarVentaRequest;
import yporque.request.DevolucionRequest;
import yporque.request.VentaRequest;
import yporque.utils.VentaFunction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
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
@ContextConfiguration(classes = {MemoryDBConfig.class,VentaController.class, VentaFunction.class})
public class VentaControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ResumenRepository resumenRepository;

    @Autowired
    private VentaController ventaController;


    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(ventaController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();

    }

    @After
    public void tearDown() throws Exception {
        articuloRepository.deleteAll();
        ventaRepository.deleteAll();
        resumenRepository.deleteAll();
    }

    @Test
    public void test_confirmar_venta() throws Exception {

        Articulo articulo = new Articulo("123457", "articulo 2", 10.0, 1.0, 1.0, 10, 10);
        articuloRepository.save(articulo);
        Instant fecha = Instant.parse("2016-01-18T18:00:00Z");
        Venta ventaDevuelta = new Venta(fecha,"123457","articulo 2",1,1.0,1.0,10.0,10.0,TipoDePago.EFECTIVO,"username1","111");
        ventaDevuelta = ventaRepository.saveAndFlush(ventaDevuelta);

        String codigoDevolucion1 = ventaDevuelta.getCodigoDevolucion();


        articulo = new Articulo("123456", "articulo 1", 10.0, 1.0, 1.0, 10, 10);
        articulo = articuloRepository.save(articulo);
        Vendedor vendedor = new Vendedor("username1", "1234", "nombre1", "apellido1");


        DevolucionRequest devolucionRequest = new DevolucionRequest(ventaDevuelta,1,vendedor,"Efectivo","111");

        double descuento = 20.0;
        VentaRequest ventaRequest = new VentaRequest(articulo,5, vendedor,"Efectivo", "cupon1", descuento);

        List<VentaRequest> list = new ArrayList<>();
        list.add(ventaRequest);

        List<DevolucionRequest> devolucionRequestList = new ArrayList<>();
        devolucionRequestList.add(devolucionRequest);

        ConfirmarVentaRequest params = new ConfirmarVentaRequest(list, devolucionRequestList, "Efectivo", 100.0, 150.0);

        HashMap<String,String> codigoDevolucion = ventaController.confirmar(params);

        Articulo articulo1 = articuloRepository.findOne(articulo.getArticuloId());

        List<Venta> ventas = ventaRepository.findByCodigoDevolucion(codigoDevolucion.get("codigoDevolucion"));

        List<Venta> devolucion = ventaRepository.findByCodigoDevolucion(codigoDevolucion1);

        List<Resumen> resumenList = resumenRepository.findAll();

        Assert.assertThat(resumenList, hasSize(1));
        Assert.assertThat(resumenList.get(0).getTipoPago(),is(TipoDePago.EFECTIVO));
        Assert.assertThat(resumenList.get(0).getTarjeta(),is(150.0));
        Assert.assertThat(resumenList.get(0).getEfectivo(),is(100.0));


        Assert.assertThat(codigoDevolucion.get("codigoDevolucion"),notNullValue());
        Assert.assertThat(articulo1.getCantidadStock(),is(5));
        Assert.assertThat(ventas,hasSize(6));
        Assert.assertThat(ventas.get(0).getCantidad(),is(1));
        Assert.assertThat(ventas.get(0).getTipoPago(),is(TipoDePago.EFECTIVO));
        Assert.assertThat(ventas.get(0).getCodigo(),is("123456"));
        Assert.assertThat(ventas.get(0).getDescripcion(),is("articulo 1"));
        Assert.assertThat(ventas.get(0).getUsername(),is("username1"));
        Assert.assertThat(ventas.get(0).getPrecioLista(),is(10.0));
        Assert.assertThat(ventas.get(0).getFactor1(),is(1.0));
        Assert.assertThat(ventas.get(0).getFactor2(),is(1.0));
        Assert.assertThat(ventas.get(0).getPrecio(),is(8.0));
        Assert.assertThat(ventas.get(0).getNroCupon(),is("cupon1"));

        Assert.assertThat(devolucion,hasSize(0));

    }

    @Test
    public void test_ventas() throws Exception {

        Instant start = Instant.parse("2015-12-24T13:00:00Z");
        Instant end = Instant.parse("2015-12-24T16:00:00Z");

        Articulo articulo = new Articulo("123456", "articulo 1", 10.0, 1.0, 1.0, 10, 10);
        articulo = articuloRepository.save(articulo);
        Articulo articulo1 = new Articulo("123457", "articulo 2", 10.0, 1.0, 1.0, 10, 10);
        articulo1 = articuloRepository.save(articulo1);

        Vendedor vendedor = new Vendedor("username1", "1234", "nombre1", "apellido1");

        VentaRequest ventaRequest = new VentaRequest(articulo,1, vendedor,"Efectivo", "cupon1", 0.0);
        VentaRequest ventaRequest1 = new VentaRequest(articulo1,1, vendedor,"Efectivo", "cupon2", 0.0);

        BiFunction<Instant,VentaRequest,Venta> function = new VentaFunction();

        ventaRepository.save(function.apply(Instant.parse("2015-12-24T16:00:00Z"),ventaRequest));
        ventaRepository.save(function.apply(Instant.parse("2015-12-24T13:00:00Z"),ventaRequest1));

        Sort order = new Sort(Sort.Direction.DESC, "fecha");
        Page<Venta> page1 = ventaController.obtenerListado(start, end, new PageRequest(0, 1, order));

        Assert.assertThat(page1.getContent(),hasSize(1));
        Assert.assertThat(page1.getContent().get(0).getCantidad(),is(1));
        Assert.assertThat(page1.getContent().get(0).getTipoPago(),is(TipoDePago.EFECTIVO));
        Assert.assertThat(page1.getContent().get(0).getCodigo(),is("123456"));
        Assert.assertThat(page1.getContent().get(0).getDescripcion(),is("articulo 1"));
        Assert.assertThat(page1.getContent().get(0).getUsername(),is("username1"));
        Assert.assertThat(page1.getContent().get(0).getPrecioLista(),is(10.0));
        Assert.assertThat(page1.getContent().get(0).getFactor1(),is(1.0));
        Assert.assertThat(page1.getContent().get(0).getFactor2(),is(1.0));
        Assert.assertThat(page1.getContent().get(0).getPrecio(),is(10.0));
        Assert.assertThat(page1.getContent().get(0).getNroCupon(),is("cupon1"));
        Assert.assertThat(page1.isFirst(),is(true));
        Assert.assertThat(page1.isLast(),is(false));

        Page<Venta> page2 = ventaController.obtenerListado(start, end, new PageRequest(1, 1, order));

        Assert.assertThat(page2.getContent(),hasSize(1));
        Assert.assertThat(page2.getContent().get(0).getCantidad(),is(1));
        Assert.assertThat(page2.getContent().get(0).getTipoPago(),is(TipoDePago.EFECTIVO));
        Assert.assertThat(page2.getContent().get(0).getCodigo(),is("123457"));
        Assert.assertThat(page2.getContent().get(0).getDescripcion(),is("articulo 2"));
        Assert.assertThat(page2.getContent().get(0).getUsername(),is("username1"));
        Assert.assertThat(page2.getContent().get(0).getPrecioLista(),is(10.0));
        Assert.assertThat(page2.getContent().get(0).getFactor1(),is(1.0));
        Assert.assertThat(page2.getContent().get(0).getFactor2(),is(1.0));
        Assert.assertThat(page2.getContent().get(0).getPrecio(),is(10.0));
        Assert.assertThat(page2.getContent().get(0).getNroCupon(),is("cupon2"));
        Assert.assertThat(page2.isFirst(),is(false));
        Assert.assertThat(page2.isLast(),is(true));

    }

    @Test
    public void test_ventas_end_and_start_null() throws Exception {

        Instant start = null;
        Instant end = null;

        Articulo articulo = new Articulo("123456", "articulo 1", 10.0, 1.0, 1.0, 10, 10);
        articulo = articuloRepository.save(articulo);
        Articulo articulo1 = new Articulo("123457", "articulo 2", 10.0, 1.0, 1.0, 10, 10);
        articulo1 = articuloRepository.save(articulo1);

        Vendedor vendedor = new Vendedor("username1", "1234", "nombre1", "apellido1");

        VentaRequest ventaRequest = new VentaRequest(articulo,1, vendedor,"Efectivo", "cupon1", 0.0);
        VentaRequest ventaRequest1 = new VentaRequest(articulo1,1, vendedor,"Efectivo", "cupon2", 0.0);

        BiFunction<Instant,VentaRequest,Venta> function = new VentaFunction();

        ventaRepository.save(function.apply(Instant.parse("2015-12-24T16:00:00Z"),ventaRequest));
        ventaRepository.save(function.apply(Instant.parse("2015-12-24T13:00:00Z"),ventaRequest1));

        Sort order = new Sort(Sort.Direction.DESC, "fecha");
        Page<Venta> page1 = ventaController.obtenerListado(start, end, new PageRequest(0, 1, order));

        Assert.assertThat(page1.getContent(),hasSize(1));
        Assert.assertThat(page1.getContent().get(0).getCantidad(),is(1));
        Assert.assertThat(page1.getContent().get(0).getTipoPago(),is(TipoDePago.EFECTIVO));
        Assert.assertThat(page1.getContent().get(0).getCodigo(),is("123456"));
        Assert.assertThat(page1.getContent().get(0).getDescripcion(),is("articulo 1"));
        Assert.assertThat(page1.getContent().get(0).getUsername(),is("username1"));
        Assert.assertThat(page1.getContent().get(0).getPrecioLista(),is(10.0));
        Assert.assertThat(page1.getContent().get(0).getFactor1(),is(1.0));
        Assert.assertThat(page1.getContent().get(0).getFactor2(),is(1.0));
        Assert.assertThat(page1.getContent().get(0).getPrecio(),is(10.0));
        Assert.assertThat(page1.getContent().get(0).getNroCupon(),is("cupon1"));
        Assert.assertThat(page1.isFirst(),is(true));
        Assert.assertThat(page1.isLast(),is(false));

        Page<Venta> page2 = ventaController.obtenerListado(start, end, new PageRequest(1, 1, order));

        Assert.assertThat(page2.getContent(),hasSize(1));
        Assert.assertThat(page2.getContent().get(0).getCantidad(),is(1));
        Assert.assertThat(page2.getContent().get(0).getTipoPago(),is(TipoDePago.EFECTIVO));
        Assert.assertThat(page2.getContent().get(0).getCodigo(),is("123457"));
        Assert.assertThat(page2.getContent().get(0).getDescripcion(),is("articulo 2"));
        Assert.assertThat(page2.getContent().get(0).getUsername(),is("username1"));
        Assert.assertThat(page2.getContent().get(0).getPrecioLista(),is(10.0));
        Assert.assertThat(page2.getContent().get(0).getFactor1(),is(1.0));
        Assert.assertThat(page2.getContent().get(0).getFactor2(),is(1.0));
        Assert.assertThat(page2.getContent().get(0).getPrecio(),is(10.0));
        Assert.assertThat(page2.getContent().get(0).getNroCupon(),is("cupon2"));
        Assert.assertThat(page2.isFirst(),is(false));
        Assert.assertThat(page2.isLast(),is(true));

    }


    @Test
    public void test_venta_search() throws Exception {

        Articulo articulo = new Articulo("123456", "articulo 1", 10.0, 1.0, 1.0, 10, 10);
        articulo = articuloRepository.save(articulo);
        Articulo articulo1 = new Articulo("123457", "articulo 2", 10.0, 1.0, 1.0, 10, 10);
        articulo1 = articuloRepository.save(articulo1);

        Vendedor vendedor = new Vendedor("username1", "1234", "nombre1", "apellido1");

        VentaRequest ventaRequest = new VentaRequest(articulo,1, vendedor,"Efectivo", "cupon1", 0.0);
        VentaRequest ventaRequest1 = new VentaRequest(articulo1,1, vendedor,"Efectivo", "cupon2", 0.0);

        BiFunction<Instant,VentaRequest,Venta> function = new VentaFunction();

        ventaRepository.save(function.apply(Instant.parse("2015-12-24T16:00:00Z"),ventaRequest));
        ventaRepository.save(function.apply(Instant.parse("2015-12-24T13:00:00Z"),ventaRequest1));

        Sort order = new Sort(Sort.Direction.DESC, "fecha");
        Page<Venta> page1 = ventaController.filtrar("123456", Instant.parse("2015-12-24T15:00:00Z"), Instant.parse("2015-12-24T17:00:00Z"), new PageRequest(0, 1, order));

        Assert.assertThat(page1.getContent(),hasSize(1));
        Assert.assertThat(page1.getContent().get(0).getCantidad(),is(1));
        Assert.assertThat(page1.getContent().get(0).getTipoPago(),is(TipoDePago.EFECTIVO));
        Assert.assertThat(page1.getContent().get(0).getCodigo(),is("123456"));
        Assert.assertThat(page1.getContent().get(0).getDescripcion(),is("articulo 1"));
        Assert.assertThat(page1.getContent().get(0).getUsername(),is("username1"));
        Assert.assertThat(page1.getContent().get(0).getPrecioLista(),is(10.0));
        Assert.assertThat(page1.getContent().get(0).getFactor1(),is(1.0));
        Assert.assertThat(page1.getContent().get(0).getFactor2(),is(1.0));
        Assert.assertThat(page1.getContent().get(0).getPrecio(),is(10.0));
        Assert.assertThat(page1.getContent().get(0).getNroCupon(),is("cupon1"));
        Assert.assertThat(page1.isFirst(),is(true));
        Assert.assertThat(page1.isLast(),is(true));

    }


    @Test
    public void test_venta_search_end_and_start_null() throws Exception {

        Articulo articulo = new Articulo("123456", "articulo 1", 10.0, 1.0, 1.0, 10, 10);
        articulo = articuloRepository.save(articulo);
        Articulo articulo1 = new Articulo("123457", "articulo 2", 10.0, 1.0, 1.0, 10, 10);
        articulo1 = articuloRepository.save(articulo1);

        Vendedor vendedor = new Vendedor("username1", "1234", "nombre1", "apellido1");

        VentaRequest ventaRequest = new VentaRequest(articulo,1, vendedor,"Efectivo", "cupon1", 0.0);
        VentaRequest ventaRequest1 = new VentaRequest(articulo1,1, vendedor,"Efectivo", "cupon2", 0.0);

        BiFunction<Instant,VentaRequest,Venta> function = new VentaFunction();

        ventaRepository.save(function.apply(Instant.parse("2015-12-24T16:00:00Z"),ventaRequest));
        ventaRepository.save(function.apply(Instant.parse("2015-12-24T13:00:00Z"),ventaRequest1));

        Sort order = new Sort(Sort.Direction.DESC, "fecha");
        Page<Venta> page1 = ventaController.filtrar("123456", null, null, new PageRequest(0, 10, order));

        Assert.assertThat(page1.getContent(),hasSize(1));
        Assert.assertThat(page1.getContent().get(0).getCantidad(),is(1));
        Assert.assertThat(page1.getContent().get(0).getTipoPago(),is(TipoDePago.EFECTIVO));
        Assert.assertThat(page1.getContent().get(0).getCodigo(),is("123456"));
        Assert.assertThat(page1.getContent().get(0).getDescripcion(),is("articulo 1"));
        Assert.assertThat(page1.getContent().get(0).getUsername(),is("username1"));
        Assert.assertThat(page1.getContent().get(0).getPrecioLista(),is(10.0));
        Assert.assertThat(page1.getContent().get(0).getFactor1(),is(1.0));
        Assert.assertThat(page1.getContent().get(0).getFactor2(),is(1.0));
        Assert.assertThat(page1.getContent().get(0).getPrecio(),is(10.0));
        Assert.assertThat(page1.getContent().get(0).getNroCupon(),is("cupon1"));
        Assert.assertThat(page1.isFirst(),is(true));
        Assert.assertThat(page1.isLast(),is(true));

    }

    @Test
    public void test_venta_search_codigo_devolucion() throws Exception {

        Articulo articulo = new Articulo("123456", "articulo 1", 10.0, 1.0, 1.0, 10, 10);
        articulo = articuloRepository.save(articulo);
        Articulo articulo1 = new Articulo("123457", "articulo 2", 10.0, 1.0, 1.0, 10, 10);
        articulo1 = articuloRepository.save(articulo1);

        Vendedor vendedor = new Vendedor("username1", "1234", "nombre1", "apellido1");

        VentaRequest ventaRequest = new VentaRequest(articulo,1, vendedor,"Efectivo", "cupon1", 0.0);
        VentaRequest ventaRequest1 = new VentaRequest(articulo1,1, vendedor,"Efectivo", "cupon2", 0.0);

        BiFunction<Instant,VentaRequest,Venta> function = new VentaFunction();

        Instant fecha = Instant.parse("2015-12-24T16:00:00Z");
        ventaRepository.save(function.apply(fecha,ventaRequest));
        Instant fecha1 = Instant.parse("2015-12-24T13:00:00Z");
        ventaRepository.save(function.apply(fecha1,ventaRequest1));

        String codigoDevolucion = String.format("%x", fecha.getEpochSecond());
        List<Venta> list = ventaController.filtrarPorCodigoDevolucion(codigoDevolucion);

        Assert.assertThat(list,hasSize(1));
        Assert.assertThat(list.get(0).getCantidad(),is(1));
        Assert.assertThat(list.get(0).getTipoPago(),is(TipoDePago.EFECTIVO));
        Assert.assertThat(list.get(0).getCodigo(),is("123456"));
        Assert.assertThat(list.get(0).getDescripcion(),is("articulo 1"));
        Assert.assertThat(list.get(0).getUsername(),is("username1"));
        Assert.assertThat(list.get(0).getPrecioLista(),is(10.0));
        Assert.assertThat(list.get(0).getFactor1(),is(1.0));
        Assert.assertThat(list.get(0).getFactor2(),is(1.0));
        Assert.assertThat(list.get(0).getPrecio(),is(10.0));
        Assert.assertThat(list.get(0).getNroCupon(),is("cupon1"));
    }


}
