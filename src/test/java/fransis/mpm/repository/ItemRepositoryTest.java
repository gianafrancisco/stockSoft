/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.repository;

import fransis.mpm.config.MemoryDBConfig;
import fransis.mpm.model.Articulo;
import fransis.mpm.model.Estado;
import fransis.mpm.model.Item;
import fransis.mpm.model.Reserva;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

/**
 * Created by francisco on 04/12/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("fransis.mpm.model")
@ContextConfiguration(classes = {MemoryDBConfig.class})
public class ItemRepositoryTest {

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ReservaRepository reservaRepository;


    @After
    public void tearDown() throws Exception {
        itemRepository.deleteAll();
        reservaRepository.deleteAll();
        articuloRepository.deleteAll();
    }

    private Articulo articulo = null;
    private Reserva reserva = null;

    @Before
    public void setUp() throws Exception {
        articulo = new Articulo("codigo 1", "Articulo 1");
        articulo = articuloRepository.saveAndFlush(articulo);
        reserva = new Reserva("Reserva 1", "r@domain.com", null, LocalDate.now().toEpochDay());
        reserva = reservaRepository.saveAndFlush(reserva);
    }

    @Test
    public void test_insert_item() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        item.setEstado(Estado.DISPONIBLE);
        Item item1 = itemRepository.save(item);
        List<Item> itemsList = itemRepository.findAll();
        Assert.assertThat(itemsList.size(),is(1));
        Assert.assertThat(itemsList.get(0).getEstado(),is(Estado.DISPONIBLE));
        Assert.assertThat(itemsList.get(0).getArticulo().getDescripcion(),is(articulo.getDescripcion()));
    }

    @Test
    public void test_update_item() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        item.setEstado(Estado.DISPONIBLE);
        Item item1 = itemRepository.save(item);
        item1.setEstado(Estado.RESERVADO);
        itemRepository.save(item1);
        List<Item> itemsList = itemRepository.findAll();
        Assert.assertThat(itemsList.size(),is(1));
        Assert.assertThat(itemsList.get(0).getEstado(),is(Estado.RESERVADO));
    }

    @Test
    public void test_delete_item() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        Item item1 = itemRepository.save(item);
        itemRepository.delete(item1);
        List<Item> itemsList = itemRepository.findAll();
        Assert.assertThat(itemsList.size(),is(0));
    }

    @Test
    public void test_item_findByArticulo() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        Item item1 = itemRepository.save(item);
        Page<Item> itemsList = itemRepository.findByArticulo(articulo, new PageRequest(0, 10));
        Assert.assertThat(itemsList.getContent().size(),is(1));
        Assert.assertThat(itemsList.getContent().get(0).getId(),is(item1.getId()));
    }

    @Test
    public void test_item_findByArticulo_not_found() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);

        Articulo articulo1 = new Articulo();
        articulo1.setDescripcion("Articulo 2");
        articulo1 = articuloRepository.saveAndFlush(articulo1);

        Item item1 = itemRepository.save(item);
        Page<Item> itemsList = itemRepository.findByArticulo(articulo1, new PageRequest(0, 10));
        Assert.assertThat(itemsList.getContent().size(),is(0));
    }

    @Test
    public void test_item_findByArticuloAndEstado() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        Item item1 = itemRepository.save(item);
        Page<Item> itemsList = itemRepository.findByArticuloAndEstadoOrderByTipoDesc(articulo, Estado.DISPONIBLE, new PageRequest(0, 10));
        Assert.assertThat(itemsList.getContent().size(),is(1));
        Assert.assertThat(itemsList.getContent().get(0).getId(),is(item1.getId()));
    }

    @Test
    public void test_item_findByArticuloAndEstado_not_found() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        Item item1 = itemRepository.save(item);
        Page<Item> itemsList = itemRepository.findByArticuloAndEstadoOrderByTipoDesc(articulo, Estado.RESERVADO, new PageRequest(0, 10));
        Assert.assertThat(itemsList.getContent().size(),is(0));
    }

    @Test
    public void test_item_findByOrdenDeCompra() throws Exception {
        Item item = new Item();
        item.setOrdenDeCompra("X-0001");
        item.setArticulo(articulo);
        Item item1 = itemRepository.save(item);
        Page<Item> itemsList = itemRepository.findByOrdenDeCompraContainingIgnoreCase("-000", new PageRequest(0, 10));
        Assert.assertThat(itemsList.getContent().size(),is(1));
        Assert.assertThat(itemsList.getContent().get(0).getId(),is(item1.getId()));
    }

    @Test
    public void test_item_findByOrdenDeCompra_not_found() throws Exception {
        Item item = new Item();
        item.setOrdenDeCompra("X-0002");
        item.setArticulo(articulo);
        Item item1 = itemRepository.save(item);
        Page<Item> itemsList = itemRepository.findByOrdenDeCompraContainingIgnoreCase("X-0001", new PageRequest(0, 10));
        Assert.assertThat(itemsList.getContent().size(),is(0));
    }

    @Test
    public void test_item_findByReserva() throws Exception {
        Item item = new Item();
        item.setOrdenDeCompra("X-0001");
        item.setArticulo(articulo);
        item.setReserva(reserva);
        Item item1 = itemRepository.save(item);
        List<Item> itemsList = itemRepository.findByReserva(reserva);
        Assert.assertThat(itemsList.size(),is(1));
        Assert.assertThat(itemsList.get(0).getId(),is(item1.getId()));
    }

    @Test
    public void test_item_findByReserva_not_found() throws Exception {
        Item item = new Item();
        item.setOrdenDeCompra("X-0002");
        item.setArticulo(articulo);
        Item item1 = itemRepository.save(item);
        List<Item> itemsList = itemRepository.findByReserva(reserva);
        Assert.assertThat(itemsList.size(),is(0));
    }

    @Test
    public void test_item_search_by_orden_de_compra() throws Exception {
        Item item = new Item();
        item.setOrdenDeCompra("X-0001");
        item.setArticulo(articulo);
        Item item1 = itemRepository.save(item);

        item = new Item();
        item.setOrdenDeCompra("X-0001");
        item.setArticulo(articulo);
        item.setEstado(Estado.VENDIDO);
        itemRepository.save(item);

        item = new Item();
        item.setOrdenDeCompra("X-0001");
        item.setArticulo(articulo);
        item.setEstado(Estado.CANCELADO);
        itemRepository.save(item);


        ArrayList<Estado> estados = new ArrayList<>();
        estados.add(Estado.DISPONIBLE);
        estados.add(Estado.RESERVADO);
        Page<Item> itemsList = itemRepository.search("-000", estados, new PageRequest(0, 10));
        Assert.assertThat(itemsList.getContent().size(),is(1));
        Assert.assertThat(itemsList.getContent().get(0).getId(),is(item1.getId()));
    }

    @Test
    public void test_item_search_by_articulo_by_codigo() throws Exception {
        Item item = new Item();
        item.setOrdenDeCompra("X-0001");
        item.setArticulo(articulo);
        Item item1 = itemRepository.save(item);

        item = new Item();
        item.setOrdenDeCompra("X-0001");
        item.setArticulo(articulo);
        item.setEstado(Estado.VENDIDO);
        itemRepository.save(item);

        item = new Item();
        item.setOrdenDeCompra("X-0001");
        item.setArticulo(articulo);
        item.setEstado(Estado.CANCELADO);
        itemRepository.save(item);


        ArrayList<Estado> estados = new ArrayList<>();
        estados.add(Estado.DISPONIBLE);
        estados.add(Estado.RESERVADO);
        Page<Item> itemsList = itemRepository.search("odigo 1", estados, new PageRequest(0, 10));
        Assert.assertThat(itemsList.getContent().size(),is(1));
        Assert.assertThat(itemsList.getContent().get(0).getId(),is(item1.getId()));
    }

    @Test
    public void test_item_search_by_articulo_by_descripcion() throws Exception {
        Item item = new Item();
        item.setOrdenDeCompra("X-0001");
        item.setArticulo(articulo);
        Item item1 = itemRepository.save(item);

        item = new Item();
        item.setOrdenDeCompra("X-0001");
        item.setArticulo(articulo);
        item.setEstado(Estado.VENDIDO);
        itemRepository.save(item);

        item = new Item();
        item.setOrdenDeCompra("X-0001");
        item.setArticulo(articulo);
        item.setEstado(Estado.CANCELADO);
        itemRepository.save(item);


        ArrayList<Estado> estados = new ArrayList<>();
        estados.add(Estado.DISPONIBLE);
        estados.add(Estado.RESERVADO);
        Page<Item> itemsList = itemRepository.search("ticulo 1", estados, new PageRequest(0, 10));
        Assert.assertThat(itemsList.getContent().size(),is(1));
        Assert.assertThat(itemsList.getContent().get(0).getId(),is(item1.getId()));
    }



}
