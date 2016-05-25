/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.repository;

import fransis.mpm.model.Articulo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import fransis.mpm.config.MemoryDBConfig;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

/**
 * Created by francisco on 04/12/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("fransis.mpm.model")
@ContextConfiguration(classes = {MemoryDBConfig.class})
public class ArticuloRepositoryTest {

    @Autowired
    private ArticuloRepository articuloRepository;

    @After
    public void tearDown() throws Exception {
        articuloRepository.deleteAll();
    }

    @Test
    public void test_insert_articulo() throws Exception {
        Articulo art = new Articulo("1234","articulo 1",1.0,2.0,2.0);
        articuloRepository.save(art);
        Articulo a = articuloRepository.findOne(art.getArticuloId());
        Assert.assertThat(a.getDescripcion(),is("articulo 1"));
        Assert.assertThat(a.getPrecio(),is(4.0));
    }

    @Test
    public void test_update_articulo() throws Exception {
        Articulo art = new Articulo("1234","articulo 1",1.0,2.0,2.0);
        articuloRepository.save(art);
        Articulo a = articuloRepository.findOne(art.getArticuloId());
        Assert.assertThat(a.getDescripcion(),is("articulo 1"));

        a.setDescripcion("articulo x");
        articuloRepository.save(a);

        a = articuloRepository.findOne(art.getArticuloId());
        Assert.assertThat(a.getDescripcion(),is("articulo x"));
    }

    @Test
    public void test_articulo_findByDescripcionOrCodigoIgnoreCase() throws Exception {
        Articulo art = new Articulo("1234","articulo 1",1.0,2.0,2.0);
        articuloRepository.save(art);

        Page<Articulo> page = articuloRepository.findByDescripcionContainingIgnoreCaseOrCodigoContainingIgnoreCase("Rticulo 1","Rticulo 1",new PageRequest(0,10));
        Assert.assertThat(page.getContent(),hasSize(1));
        Assert.assertThat(page.getContent().get(0).getDescripcion(),is("articulo 1"));

        page = articuloRepository.findByDescripcionContainingIgnoreCaseOrCodigoContainingIgnoreCase("23","23",new PageRequest(0,10));
        Assert.assertThat(page.getContent(),hasSize(1));
        Assert.assertThat(page.getContent().get(0).getCodigo(),is("1234"));


    }


}
