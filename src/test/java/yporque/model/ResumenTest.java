package yporque.model;

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

import static org.hamcrest.core.Is.is;

/**
 * Created by francisco on 13/12/2015.
 */
public class ResumenTest {

    @Test
    public void test_new_resumen() throws Exception {
        Instant fecha = Instant.parse("2016-01-20T18:00:00Z");
        Resumen resumen = new Resumen(fecha,TipoDePago.MIXTO,60.0,70.0);

        Assert.assertThat(resumen.getFecha(),is(fecha));
        Assert.assertThat(resumen.getTipoPago(),is(TipoDePago.MIXTO));
        Assert.assertThat(resumen.getEfectivo(),is(60.0));
        Assert.assertThat(resumen.getTarjeta(),is(70.0));
    }

    @Test
    public void test_default() throws Exception {

        Resumen resumen = new Resumen();

        Assert.assertThat(resumen.getFecha(),is(Instant.EPOCH));
        Assert.assertThat(resumen.getTipoPago(),is(TipoDePago.EFECTIVO));
        Assert.assertThat(resumen.getEfectivo(),is(0.0));
        Assert.assertThat(resumen.getTarjeta(),is(0.0));
    }
}
