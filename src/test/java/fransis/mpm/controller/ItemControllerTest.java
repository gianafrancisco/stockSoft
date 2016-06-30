/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fransis.mpm.config.MemoryDBConfig;
import fransis.mpm.model.Articulo;
import fransis.mpm.model.Estado;
import fransis.mpm.model.Item;
import fransis.mpm.repository.ArticuloRepository;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URI;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by francisco on 18/12/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("yporque")
@ContextConfiguration(classes = {MemoryDBConfig.class, ItemController.class})
public class ItemControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private ItemRepository itemRepository;


    @Autowired
    private ItemController itemController;

    private Articulo articulo = null;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
        articulo = new Articulo();
        articulo.setDescripcion("Articulo 1");
        articulo = articuloRepository.saveAndFlush(articulo);
    }

    @After
    public void tearDown() throws Exception {
        itemRepository.deleteAll();
        articuloRepository.deleteAll();
    }

    @Test
    public void test_get_items() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        item = itemRepository.saveAndFlush(item);

        ResponseEntity<Page<Item>> response = itemController.obtener(new PageRequest(0,10),articulo.getArticuloId());

        Assert.assertThat(response.getStatusCode(),is(HttpStatus.OK));
        Assert.assertThat(response.getBody().getTotalElements(),is(1L));
        Assert.assertThat(response.getBody().getContent().get(0).getItemId(),is(item.getItemId()));

    }

    @Test
    public void test_get_items_integration() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        item = itemRepository.saveAndFlush(item);

        Long itemId = item.getItemId();
        mockMvc.perform(get("/articulos/"+articulo.getArticuloId()+"/items").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.content[0].estado").value(Estado.EN_STOCK.toString()))
                .andExpect(jsonPath("$.content[0].itemId").value(is(itemId.intValue())));
    }

    @Test
    public void test_get_items_not_found() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        item = itemRepository.saveAndFlush(item);

        ResponseEntity<Page<Item>> response = itemController.obtener(new PageRequest(0,10),-1L);

        Assert.assertThat(response.getStatusCode(),is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void test_get_items_not_found_integration() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        item = itemRepository.saveAndFlush(item);

        Long itemId = item.getItemId();
        mockMvc.perform(get("/articulos/0/items").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_get_items_null_articuloId() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        item = itemRepository.saveAndFlush(item);

        ResponseEntity<Page<Item>> response = itemController.obtener(new PageRequest(0,10),null);

        Assert.assertThat(response.getStatusCode(),is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void test_get_items_null_articuloId_integration() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        item = itemRepository.saveAndFlush(item);

        Long itemId = item.getItemId();
        mockMvc.perform(get("/articulos//items").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isNotFound());
    }


    @Test
    public void test_request_articulos_put_not_content() throws Exception {

        Item item = new Item();
        item.setArticulo(articulo);
        item = itemRepository.saveAndFlush(item);
        item.setEstado(Estado.RESERVADO);

        ResponseEntity<Void> responseEntity = itemController.put(articulo.getArticuloId(), item.getItemId(), item);
        Assert.assertThat(responseEntity.getStatusCode(),is(HttpStatus.NO_CONTENT));

        Item item1 = itemRepository.findOne(item.getItemId());
        Assert.assertThat(item1.getEstado(),is(Estado.RESERVADO));

    }


    @Test
    public void test_request_articulos_put_not_content_integration() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        item = itemRepository.saveAndFlush(item);

        item.setEstado(Estado.RESERVADO);

        String itemJson = mapper.writeValueAsString(item);


        Long itemId = item.getItemId();
        MockHttpServletRequestBuilder accept = MockMvcRequestBuilders.put("/articulos/" + articulo.getArticuloId() + "/items/" + item.getItemId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson.getBytes());

        mockMvc.perform(accept)
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_request_articulos_put_not_found() throws Exception {

        Item item = new Item();
        item.setArticulo(articulo);

        ResponseEntity<Void> responseEntity = itemController.put(articulo.getArticuloId(), 100L, item);
        Assert.assertThat(responseEntity.getStatusCode(),is(HttpStatus.NOT_FOUND));

    }

    @Test
    public void test_request_articulos_put_not_found_integration() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        item = itemRepository.saveAndFlush(item);

        item.setEstado(Estado.RESERVADO);

        String itemJson = mapper.writeValueAsString(item);


        Long itemId = item.getItemId();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/articulos/" + articulo.getArticuloId() + "/items/" + item.getItemId()+100)
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.getBytes());

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_request_item_agregar() throws Exception {

        Item item = new Item();

        ResponseEntity<Item> responseEntity = itemController.agregar(articulo.getArticuloId(), item);
        Assert.assertThat(responseEntity.getStatusCode(),is(HttpStatus.CREATED));
        URI location = responseEntity.getHeaders().getLocation();
        Item itemResponse = responseEntity.getBody();
        Assert.assertThat(location.toString(),is("/articulos/"+articulo.getArticuloId()+"/items/"+itemResponse.getItemId()));
        List<Item> itemList = itemRepository.findAll();
        Assert.assertThat(itemList.size(),is(1));

    }

    @Test
    public void test_request_item_agregar_integration() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        item = itemRepository.saveAndFlush(item);
        /* Calculate the next Id for item, in order to use in the integration test with mockMvc */
        Long nextId = item.getItemId() + 1;
        item = new Item();
        item.setArticulo(articulo);

        String itemJson = mapper.writeValueAsString(item);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/articulos/" + articulo.getArticuloId() + "/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.getBytes());

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value(Estado.EN_STOCK.toString()))
                .andExpect(jsonPath("$.itemId").value(is(nextId.intValue())))
                .andExpect(header().string("location",is("/articulos/"+articulo.getArticuloId()+"/items/"+nextId.intValue())));
    }


    @Test
    public void test_request_borrar_articulo() throws Exception {

        Item item = new Item();
        item.setArticulo(articulo);
        item = itemRepository.saveAndFlush(item);

        ResponseEntity<Void>  responseEntity = itemController.borrar(item.getItemId());
        Assert.assertThat(responseEntity.getStatusCode(),is(HttpStatus.OK));
    }

    @Test
    public void test_request_borrar_articulo_integration() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        item = itemRepository.saveAndFlush(item);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/articulos/" + articulo.getArticuloId() + "/items/"+item.getItemId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }


    @Test
    public void test_request_borrar_articulo_not_found() throws Exception {
        ResponseEntity<Void>  responseEntity = itemController.borrar(10L);
        Assert.assertThat(responseEntity.getStatusCode(),is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void test_request_borrar_articulo_not_found_integration() throws Exception {
        Item item = new Item();
        item.setArticulo(articulo);
        item = itemRepository.saveAndFlush(item);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/articulos/" + articulo.getArticuloId() + "/items/"+item.getItemId()+1)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }


}
