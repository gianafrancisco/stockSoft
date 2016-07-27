package fransis.mpm.model;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

/**
 * Created by francisco on 27/07/2016.
 */
public class ArticuloWebModelTest {
    @Test
    public void test_new_articulo() throws Exception {
        Articulo articulo = new Articulo("1234","articulo 1");
        ArticuloWebModel articuloWebModel = new ArticuloWebModel(articulo, 15L, 10L);
        Assert.assertThat(articuloWebModel.getDescripcion(), is("articulo 1"));
        Assert.assertThat(articuloWebModel.getCodigo(), is("1234"));
        Assert.assertThat(articuloWebModel.getStockFisico(),is(15L));
        Assert.assertThat(articuloWebModel.getStockVirtual(),is(10L));
    }
}
