package yporque.controller;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yporque.config.MemoryDBConfig;
import yporque.model.Articulo;
import yporque.repository.ArticuloRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by francisco on 18/12/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("yporque")
@ContextConfiguration(classes = {MemoryDBConfig.class, ArticuloController.class})
public class ArticuloControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private ArticuloController articuloController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(articuloController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
    }

    @After
    public void tearDown() throws Exception {
        articuloRepository.deleteAll();
    }

    @Test
    public void test_get_articulos() throws Exception {

        Articulo articulo = new Articulo("1234","articulo 1",1.0,2.0,2.0,1,1);
        articuloRepository.saveAndFlush(articulo);

        mockMvc.perform(get("/articulos").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.content[0].descripcion").value("articulo 1"));

    }


    @Test
    public void test_request_articulos() throws Exception {

        Articulo articulo = new Articulo("1234","articulo 1",1.0,2.0,2.0,1,1);
        articuloRepository.saveAndFlush(articulo);

        Page<Articulo> page = articuloController.obtenerListaArticulos(new PageRequest(0,10));

        Assert.assertThat(page.getTotalPages(),is(1));
        Assert.assertThat(page.getTotalElements(),is(1L));
        Assert.assertThat(page.getNumberOfElements(),is(1));
        Assert.assertThat(page.getContent().get(0).getCodigo(),is("1234"));
        Assert.assertThat(page.getContent().get(0).getDescripcion(),is("articulo 1"));

    }


    @Test
    public void test_request_articulo_search() throws Exception {

        Articulo articulo = new Articulo("1234","articulo 1",1.0,2.0,2.0,1,1);
        articuloRepository.saveAndFlush(articulo);

        Page<Articulo> page = articuloController.filtrarArticulos("ticulo",new PageRequest(0,10));

        Assert.assertThat(page.getTotalPages(),is(1));
        Assert.assertThat(page.getTotalElements(),is(1L));
        Assert.assertThat(page.getNumberOfElements(),is(1));
        Assert.assertThat(page.getContent().get(0).getCodigo(),is("1234"));
        Assert.assertThat(page.getContent().get(0).getDescripcion(),is("articulo 1"));

    }

    @Test
    public void test_request_articulo_agregar() throws Exception {

        Articulo articulo = new Articulo("1234","articulo 1",1.0,2.0,2.0,1,1);

        articuloController.agregar(articulo);

        Page<Articulo> page = articuloController.obtenerListaArticulos(new PageRequest(0,10));

        Assert.assertThat(page.getTotalPages(),is(1));
        Assert.assertThat(page.getTotalElements(),is(1L));
        Assert.assertThat(page.getNumberOfElements(),is(1));
        Assert.assertThat(page.getContent().get(0).getCodigo(),is("1234"));
        Assert.assertThat(page.getContent().get(0).getDescripcion(),is("articulo 1"));

    }


    @Test
    public void test_request_borrar_articulo() throws Exception {

        Articulo articulo = new Articulo("1234","articulo 1",1.0,2.0,2.0,1,1);

        articulo = articuloController.agregar(articulo);

        articuloController.borrarArticulo(articulo.getArticuloId());

        Page<Articulo> page = articuloController.obtenerListaArticulos(new PageRequest(0,10));

        Assert.assertThat(page.getTotalPages(),is(0));
        Assert.assertThat(page.getTotalElements(),is(0L));
        Assert.assertThat(page.getNumberOfElements(),is(0));
        Assert.assertThat(page.getContent(),hasSize(0));

    }


}
