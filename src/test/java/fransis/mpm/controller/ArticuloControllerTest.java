/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.controller;

import fransis.mpm.function.Excel;
import fransis.mpm.function.ExportarService;
import fransis.mpm.model.Articulo;
import fransis.mpm.model.Estado;
import fransis.mpm.model.Item;
import fransis.mpm.model.Tipo;
import fransis.mpm.repository.ItemRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import fransis.mpm.config.MemoryDBConfig;
import fransis.mpm.repository.ArticuloRepository;

import java.net.URI;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by francisco on 18/12/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("yporque")
@ContextConfiguration(classes = {MemoryDBConfig.class, ArticuloController.class, Excel.class})
public class ArticuloControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ArticuloController articuloController;



    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(articuloController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
    }

    @After
    public void tearDown() throws Exception {
        itemRepository.deleteAll();
        articuloRepository.deleteAll();
    }

    @Test
    public void test_get_articulos() throws Exception {

        Articulo articulo = new Articulo("1234","articulo 1");
        articulo = articuloRepository.saveAndFlush(articulo);

        Item item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123456");
        item.setEstado(Estado.DISPONIBLE);
        itemRepository.saveAndFlush(item);

        item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123459");
        item.setEstado(Estado.DISPONIBLE);
        item.setTipo(Tipo.FISICO);
        itemRepository.saveAndFlush(item);

        item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123458");
        item.setEstado(Estado.RESERVADO);
        itemRepository.saveAndFlush(item);

        mockMvc.perform(
                get("/articulos").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.content[0].descripcion").value("articulo 1"))
                .andExpect(jsonPath("$.content[0].stockVirtual").value(1))
                .andExpect(jsonPath("$.content[0].stockFisico").value(1));

    }

    @Test
    public void test_get_articulo() throws Exception {

        Articulo articulo = new Articulo("1234","articulo 1");
        articulo = articuloRepository.saveAndFlush(articulo);

        mockMvc.perform(
                get("/articulos/"+articulo.getArticuloId()).accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.descripcion").value("articulo 1"))
                .andExpect(jsonPath("$.articuloId").value(is(articulo.getArticuloId().intValue())));

    }

    @Test
    public void test_get_articulo_not_found() throws Exception {

        mockMvc.perform(
                get("/articulos/99999").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isNotFound());

    }


    @Test
    public void test_request_articulos() throws Exception {

        Articulo articulo = new Articulo("1234","articulo 1");
        articulo = articuloRepository.saveAndFlush(articulo);

        Item item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123456");
        item.setEstado(Estado.DISPONIBLE);
        itemRepository.saveAndFlush(item);

        item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123459");
        item.setEstado(Estado.DISPONIBLE);
        item.setTipo(Tipo.FISICO);
        itemRepository.saveAndFlush(item);

        item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123458");
        item.setEstado(Estado.RESERVADO);
        itemRepository.saveAndFlush(item);

        item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123453");
        item.setEstado(Estado.RESERVADO);
        item.setTipo(Tipo.FISICO);
        itemRepository.saveAndFlush(item);


        Page<Articulo> page = articuloController.obtenerListaArticulos(new PageRequest(0,10));

        Assert.assertThat(page.getTotalPages(),is(1));
        Assert.assertThat(page.getTotalElements(),is(1L));
        Assert.assertThat(page.getNumberOfElements(),is(1));
        Assert.assertThat(page.getContent().get(0).getCodigo(),is("1234"));
        Assert.assertThat(page.getContent().get(0).getDescripcion(),is("articulo 1"));
        Assert.assertThat(page.getContent().get(0).getStockVirtual(),is(1L));
        Assert.assertThat(page.getContent().get(0).getStockFisico(),is(1L));
        Assert.assertThat(page.getContent().get(0).getStockVirtualReservado(),is(1L));
        Assert.assertThat(page.getContent().get(0).getStockFisicoReservado(),is(1L));

    }

    @Test
    public void test_request_articulo_controller() throws Exception {

        Articulo articulo = new Articulo("1234","articulo 1");
        articulo = articuloRepository.saveAndFlush(articulo);

        Item item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123456");
        item.setEstado(Estado.DISPONIBLE);
        itemRepository.saveAndFlush(item);

        item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123459");
        item.setEstado(Estado.DISPONIBLE);
        item.setTipo(Tipo.FISICO);
        itemRepository.saveAndFlush(item);

        item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123458");
        item.setEstado(Estado.RESERVADO);
        itemRepository.saveAndFlush(item);

        item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123453");
        item.setEstado(Estado.RESERVADO);
        item.setTipo(Tipo.FISICO);
        itemRepository.saveAndFlush(item);


        ResponseEntity<Articulo> responseEntity = articuloController.obtener(articulo.getArticuloId());
        Articulo articulo1 = responseEntity.getBody();

        Assert.assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        Assert.assertThat(articulo1.getCodigo(),is("1234"));
        Assert.assertThat(articulo1.getDescripcion(),is("articulo 1"));
        Assert.assertThat(articulo1.getStockVirtual(),is(1L));
        Assert.assertThat(articulo1.getStockFisico(),is(1L));
        Assert.assertThat(articulo1.getStockVirtualReservado(),is(1L));
        Assert.assertThat(articulo1.getStockFisicoReservado(),is(1L));

    }


    @Test
    public void test_request_articulos_put_not_content() throws Exception {

        Articulo articulo = new Articulo("1234","articulo 1");
        articulo = articuloRepository.saveAndFlush(articulo);
        articulo.setDescripcion("articulo 20");
        articulo.setPrecioDeInventario(30.90);
        long actualizacion = articulo.getFechaActualizacion();

        ResponseEntity<Void> responseEntity = articuloController.put(articulo);
        Articulo articulo1 = articuloRepository.findOne(articulo.getArticuloId());

        Assert.assertThat(responseEntity.getStatusCode(),is(HttpStatus.NO_CONTENT));
        Assert.assertThat(articulo1.getDescripcion(),is(articulo.getDescripcion()));
        Assert.assertThat(articulo1.getPrecioDeInventario(), is(articulo.getPrecioDeInventario()));
        Assert.assertThat(articulo1.getFechaActualizacion(), is(not(actualizacion)));

    }

    @Test
    public void test_request_articulos_put_not_found() throws Exception {

        Articulo articulo = new Articulo("1234","articulo 1");
        ResponseEntity<Void> responseEntity = articuloController.put(articulo);
        Assert.assertThat(responseEntity.getStatusCode(),is(HttpStatus.NOT_FOUND));

    }


    @Test
    public void test_request_articulo_search() throws Exception {

        Articulo articulo = new Articulo("1234","articulo 1");
        articuloRepository.saveAndFlush(articulo);

        Item item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123456");
        item.setEstado(Estado.DISPONIBLE);
        itemRepository.saveAndFlush(item);

        item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123459");
        item.setEstado(Estado.DISPONIBLE);
        item.setTipo(Tipo.FISICO);
        itemRepository.saveAndFlush(item);

        item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123458");
        item.setEstado(Estado.RESERVADO);
        itemRepository.saveAndFlush(item);

        item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123453");
        item.setEstado(Estado.RESERVADO);
        item.setTipo(Tipo.FISICO);
        itemRepository.saveAndFlush(item);


        Page<Articulo> page = articuloController.filtrarArticulos("ticulo",new PageRequest(0,10));

        Assert.assertThat(page.getTotalPages(),is(1));
        Assert.assertThat(page.getTotalElements(),is(1L));
        Assert.assertThat(page.getNumberOfElements(),is(1));
        Assert.assertThat(page.getContent().get(0).getCodigo(),is("1234"));
        Assert.assertThat(page.getContent().get(0).getDescripcion(),is("articulo 1"));
        Assert.assertThat(page.getContent().get(0).getStockFisico(),is(1L));
        Assert.assertThat(page.getContent().get(0).getStockVirtual(),is(1L));
        Assert.assertThat(page.getContent().get(0).getStockFisicoReservado(),is(1L));
        Assert.assertThat(page.getContent().get(0).getStockVirtualReservado(),is(1L));

    }

    @Test
    public void test_request_articulo_codigo() throws Exception {

        Articulo articulo = new Articulo("1234","articulo 1");
        articuloRepository.saveAndFlush(articulo);

        Item item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123456");
        item.setEstado(Estado.DISPONIBLE);
        itemRepository.saveAndFlush(item);

        item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123459");
        item.setEstado(Estado.DISPONIBLE);
        item.setTipo(Tipo.FISICO);
        itemRepository.saveAndFlush(item);

        item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123458");
        item.setEstado(Estado.RESERVADO);
        itemRepository.saveAndFlush(item);

        item = new Item();
        item.setArticulo(articulo);
        item.setOrdenDeCompra("123453");
        item.setEstado(Estado.RESERVADO);
        item.setTipo(Tipo.FISICO);
        itemRepository.saveAndFlush(item);


        ResponseEntity<List<Articulo>> page = articuloController.byCodigo("1234");

        Assert.assertThat(page.getStatusCode(),is(HttpStatus.OK));
        Assert.assertThat(page.getBody().get(0).getCodigo(),is("1234"));
        Assert.assertThat(page.getBody().get(0).getDescripcion(),is("articulo 1"));
        Assert.assertThat(page.getBody().get(0).getStockFisico(),is(1L));
        Assert.assertThat(page.getBody().get(0).getStockVirtual(),is(1L));
        Assert.assertThat(page.getBody().get(0).getStockFisicoReservado(),is(1L));
        Assert.assertThat(page.getBody().get(0).getStockVirtualReservado(),is(1L));

    }

    @Test
    public void test_request_articulo_codigo_not_found() throws Exception {

        Articulo articulo = new Articulo("1234","articulo 1");
        articuloRepository.saveAndFlush(articulo);

        ResponseEntity<List<Articulo>> page = articuloController.byCodigo("123");

        Assert.assertThat(page.getStatusCode(),is(HttpStatus.NOT_FOUND));

    }

    @Test
    public void test_request_articulo_agregar() throws Exception {

        Articulo articulo = new Articulo("1234","articulo 1");

        ResponseEntity<Articulo> responseEntity = articuloController.agregar(articulo);
        Assert.assertThat(responseEntity.getStatusCode(),is(HttpStatus.CREATED));
        URI location = responseEntity.getHeaders().getLocation();
        articulo = responseEntity.getBody();
        Assert.assertThat(location.toString(),is("/articulos/"+articulo.getArticuloId()));

        Page<Articulo> page = articuloController.obtenerListaArticulos(new PageRequest(0,10));

        Assert.assertThat(page.getTotalPages(),is(1));
        Assert.assertThat(page.getTotalElements(),is(1L));
        Assert.assertThat(page.getNumberOfElements(),is(1));
        Assert.assertThat(page.getContent().get(0).getCodigo(),is("1234"));
        Assert.assertThat(page.getContent().get(0).getDescripcion(),is("articulo 1"));

    }

    @Test
    public void test_request_borrar_articulo() throws Exception {

        Articulo articulo = new Articulo("1234","articulo 1");

        ResponseEntity<Articulo> responseEntity = articuloController.agregar(articulo);
        articulo = responseEntity.getBody();


        ResponseEntity<Void> responseDelete = articuloController.borrarArticulo(articulo.getArticuloId());
        Assert.assertThat(responseDelete.getStatusCode(),is(HttpStatus.OK));

        Page<Articulo> page = articuloController.obtenerListaArticulos(new PageRequest(0,10));

        Assert.assertThat(page.getTotalPages(),is(0));
        Assert.assertThat(page.getTotalElements(),is(0L));
        Assert.assertThat(page.getNumberOfElements(),is(0));
        Assert.assertThat(page.getContent(),hasSize(0));
    }

    @Test
    public void test_request_borrar_articulo_not_found() throws Exception {
        ResponseEntity<Void>  responseEntity = articuloController.borrarArticulo(10L);
        Assert.assertThat(responseEntity.getStatusCode(),is(HttpStatus.NOT_FOUND));
    }


}
