/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.controller;

import fransis.mpm.model.Vendedor;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import fransis.mpm.config.MemoryDBConfig;
import fransis.mpm.repository.VendedorRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Created by francisco on 18/12/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("yporque")
@ContextConfiguration(classes = {MemoryDBConfig.class, VendedorController.class})
public class VendedorControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private VendedorController vendedorController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(vendedorController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
    }

    @After
    public void tearDown() throws Exception {
        vendedorRepository.deleteAll();
    }

    @Test
    public void test_obtener_vendedores() throws Exception {

        vendedorRepository.save(new Vendedor("username1","1234","nombre1","apellido1"));

        Page<Vendedor> page = vendedorController.obtenerLista(new PageRequest(0,10));

        Assert.assertThat(page.getTotalPages(),is(1));
        Assert.assertThat(page.getTotalElements(),is(1L));
        Assert.assertThat(page.getNumberOfElements(),is(1));
        Assert.assertThat(page.getContent().get(0).getUsername(),is("username1"));
        Assert.assertThat(page.getContent().get(0).getPassword(),is("1234"));
        Assert.assertThat(page.getContent().get(0).getNombre(),is("nombre1"));
        Assert.assertThat(page.getContent().get(0).getApellido(),is("apellido1"));

    }


    @Test
    public void test_obtener_vendedor_search() throws Exception {

        vendedorRepository.save(new Vendedor("username1","1234","nombre1","apellido1"));

        Page<Vendedor> page = vendedorController.filtrar("name1",new PageRequest(0,10));

        Assert.assertThat(page.getTotalPages(),is(1));
        Assert.assertThat(page.getTotalElements(),is(1L));
        Assert.assertThat(page.getNumberOfElements(),is(1));
        Assert.assertThat(page.getContent().get(0).getUsername(),is("username1"));
        Assert.assertThat(page.getContent().get(0).getPassword(),is("1234"));
        Assert.assertThat(page.getContent().get(0).getNombre(),is("nombre1"));
        Assert.assertThat(page.getContent().get(0).getApellido(),is("apellido1"));

    }


    @Test
    public void test_obtener_vendedor_agregar() throws Exception {

        Vendedor vendedor = new Vendedor("username1","1234","nombre1","apellido1");

        vendedorController.agregar(vendedor);

        Page<Vendedor> page = vendedorController.obtenerLista(new PageRequest(0,10));

        Assert.assertThat(page.getTotalPages(),is(1));
        Assert.assertThat(page.getTotalElements(),is(1L));
        Assert.assertThat(page.getNumberOfElements(),is(1));
        Assert.assertThat(page.getContent().get(0).getUsername(),is("username1"));
        Assert.assertThat(page.getContent().get(0).getPassword(),is("1234"));
        Assert.assertThat(page.getContent().get(0).getNombre(),is("nombre1"));
        Assert.assertThat(page.getContent().get(0).getApellido(),is("apellido1"));

    }

    @Test
    public void test_obtener_vendedor_delete() throws Exception {

        Vendedor vendedor = new Vendedor("username1","1234","nombre1","apellido1");

        vendedor = vendedorController.agregar(vendedor);

        Page<Vendedor> page = vendedorController.obtenerLista(new PageRequest(0,10));

        Assert.assertThat(page.getTotalPages(),is(1));
        Assert.assertThat(page.getTotalElements(),is(1L));
        Assert.assertThat(page.getNumberOfElements(),is(1));
        Assert.assertThat(page.getContent().get(0).getUsername(),is("username1"));
        Assert.assertThat(page.getContent().get(0).getPassword(),is("1234"));
        Assert.assertThat(page.getContent().get(0).getNombre(),is("nombre1"));
        Assert.assertThat(page.getContent().get(0).getApellido(),is("apellido1"));


        vendedorController.delete(vendedor.getVendedorId());

        page = vendedorController.obtenerLista(new PageRequest(0,10));

        Assert.assertThat(page.getTotalPages(),is(0));
        Assert.assertThat(page.getTotalElements(),is(0L));
        Assert.assertThat(page.getNumberOfElements(),is(0));
        Assert.assertThat(page.getContent(),hasSize(0));

    }


    @Test
    public void test_obtener_vendedor_do_not_delete_administrador() throws Exception {

        Vendedor vendedor = new Vendedor("administrador","1234","nombre1","apellido1");

        vendedor = vendedorRepository.save(vendedor);

        vendedorController.delete(vendedor.getVendedorId());

        Page<Vendedor> page = vendedorController.obtenerLista(new PageRequest(0,10));

        Assert.assertThat(page.getTotalPages(),is(1));
        Assert.assertThat(page.getTotalElements(),is(1L));
        Assert.assertThat(page.getNumberOfElements(),is(1));
        Assert.assertThat(page.getContent().get(0).getUsername(),is("administrador"));
        Assert.assertThat(page.getContent().get(0).getPassword(),is("1234"));
        Assert.assertThat(page.getContent().get(0).getNombre(),is("nombre1"));
        Assert.assertThat(page.getContent().get(0).getApellido(),is("apellido1"));

    }

}
