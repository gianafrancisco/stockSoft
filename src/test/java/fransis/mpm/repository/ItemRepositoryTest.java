/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.repository;

import fransis.mpm.config.MemoryDBConfig;
import fransis.mpm.model.Articulo;
import fransis.mpm.model.Estado;
import fransis.mpm.model.Item;
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


    @After
    public void tearDown() throws Exception {
        itemRepository.deleteAll();
        articuloRepository.deleteAll();
    }

    private Articulo articulo = null;

    @Before
    public void setUp() throws Exception {
        articulo = new Articulo();
        articulo.setDescripcion("Articulo 1");
        articulo = articuloRepository.saveAndFlush(articulo);
    }

    @Test
    public void test_insert_item() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        item.setEstado(Estado.EN_STOCK);
        Item item1 = itemRepository.save(item);
        List<Item> itemsList = itemRepository.findAll();
        Assert.assertThat(itemsList.size(),is(1));
        Assert.assertThat(itemsList.get(0).getEstado(),is(Estado.EN_STOCK));
        Assert.assertThat(itemsList.get(0).getArticulo().getDescripcion(),is(articulo.getDescripcion()));
    }

    @Test
    public void test_update_item() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        item.setEstado(Estado.EN_STOCK);
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
        Assert.assertThat(itemsList.size(),is(0));

    }

}
