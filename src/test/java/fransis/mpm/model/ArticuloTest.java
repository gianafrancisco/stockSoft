/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.model;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

/**
 * Created by francisco on 13/12/2015.
 */
public class ArticuloTest {

    @Test
    public void test_new_articulo() throws Exception {
        Articulo articulo = new Articulo("1234","articulo 1",1.0,2.0,2.0);

        Assert.assertThat(articulo.getDescripcion(),is("articulo 1"));
        Assert.assertThat(articulo.getPrecio(),is(4.0));
        Assert.assertThat(articulo.getFactor1(),is(2.0));
        Assert.assertThat(articulo.getFactor2(),is(2.0));
        Assert.assertThat(articulo.getPrecioLista(),is(1.0));
    }

}
