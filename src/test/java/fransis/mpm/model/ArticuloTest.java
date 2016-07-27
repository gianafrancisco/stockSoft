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
        Articulo articulo = new Articulo("1234","articulo 1");
        Assert.assertThat(articulo.getDescripcion(), is("articulo 1"));
        Assert.assertThat(articulo.getCodigo(), is("1234"));
    }

}
