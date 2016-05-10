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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yporque.config.MemoryDBConfig;
import yporque.model.*;
import yporque.repository.RetiroRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

/**
 * Created by francisco on 18/12/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("yporque")
@ContextConfiguration(classes = {MemoryDBConfig.class,RetiroController.class})
public class RetiroControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private RetiroRepository retiroRepository;

    @Autowired
    private RetiroController retiroController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(retiroController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
    }

    @After
    public void tearDown() throws Exception {
        retiroRepository.deleteAll();
    }

    @Test
    public void test_agregar_retiro() throws Exception {

        Retiro retiro = new Retiro(100.0, "retiro 1", LocalDateTime.now().toInstant(ZoneOffset.UTC), "username1");

        retiroController.agregar(retiro);

        List<Retiro> retiroList = retiroRepository.findAll();

        Assert.assertThat(retiroList,hasSize(1));
        Assert.assertThat(retiroList.get(0).getUsername(),is("username1"));
        Assert.assertThat(retiroList.get(0).getDescripcion(),is("retiro 1"));
        Assert.assertThat(retiroList.get(0).getMonto(),is(100.0));
        Assert.assertThat(retiroList.get(0).getFecha(),notNullValue());

    }

    @Test
    public void test_retiros_today() throws Exception {

        Instant today = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        Retiro retiro = new Retiro(100.0,"retiro 1", today,"username1");
        retiroRepository.saveAndFlush(retiro);
        retiro = new Retiro(200.0,"retiro 2",Instant.parse("2015-12-29T10:00:00Z"),"username1");
        retiroRepository.saveAndFlush(retiro);
        retiro = new Retiro(300.0,"retiro 3",Instant.parse("2015-12-31T10:00:00Z"),"username1");
        retiroRepository.saveAndFlush(retiro);

        Page<Retiro> retiroPage = retiroController.obtenerListado(new PageRequest(0,10));

        Assert.assertThat(retiroPage.getContent(),hasSize(1));
        Assert.assertThat(retiroPage.getContent().get(0).getUsername(),is("username1"));
        Assert.assertThat(retiroPage.getContent().get(0).getDescripcion(),is("retiro 1"));
        Assert.assertThat(retiroPage.getContent().get(0).getMonto(),is(100.0));
        Assert.assertThat(retiroPage.getContent().get(0).getFecha(),is(today));
        Assert.assertThat(retiroPage.isFirst(),is(true));
        Assert.assertThat(retiroPage.isLast(),is(true));

    }

}
