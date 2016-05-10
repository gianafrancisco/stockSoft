package yporque.model;

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;


public class CajaTest {

    @Test
    public void test_new_caja() throws Exception {
        Caja caja = new Caja(Instant.parse("2015-12-30T00:00:00Z"),"username1");

        Assert.assertThat(caja.getApertura(),is(Instant.parse("2015-12-30T00:00:00Z")));
        Assert.assertThat(caja.getAperturaUsername(),is("username1"));

        Assert.assertThat(caja.getCierreUsername(),is(""));
        Assert.assertThat(caja.getCierre(),is(Instant.EPOCH));
        Assert.assertThat(caja.getEfectivo(),is(0.0));
        Assert.assertThat(caja.getTarjeta(),is(0.0));
        Assert.assertThat(caja.getTotalVentaDia(),is(0.0));
        Assert.assertThat(caja.getEfectivoDiaSiguiente(),is(0.0));
        Assert.assertThat(caja.getCajaId(),nullValue());

    }

    @Test
    public void test_new_caja_default_contructor() throws Exception {
        Caja caja = new Caja();

        Assert.assertThat(caja.getApertura(),notNullValue());
        Assert.assertThat(caja.getAperturaUsername(),is(""));

        Assert.assertThat(caja.getCierreUsername(),is(""));
        Assert.assertThat(caja.getCierre(),is(Instant.EPOCH));
        Assert.assertThat(caja.getEfectivo(),is(0.0));
        Assert.assertThat(caja.getTarjeta(),is(0.0));
        Assert.assertThat(caja.getTotalVentaDia(),is(0.0));
        Assert.assertThat(caja.getEfectivoDiaSiguiente(),is(0.0));
        Assert.assertThat(caja.getCajaId(),nullValue());

    }

    @Test
    public void test_set() throws Exception {
        Caja caja = new Caja(Instant.parse("2015-12-30T00:00:00Z"),"username1");

        caja.setCierre(Instant.MAX);
        caja.setEfectivo(10.0);
        caja.setTarjeta(200.0);
        caja.setEfectivoDiaSiguiente(200.0);
        caja.setTotalVentaDia(210.0);
        caja.setCierreUsername("username2");

        Assert.assertThat(caja.getApertura(),is(Instant.parse("2015-12-30T00:00:00Z")));
        Assert.assertThat(caja.getAperturaUsername(),is("username1"));

        Assert.assertThat(caja.getCierreUsername(),is("username2"));
        Assert.assertThat(caja.getCierre(),is(Instant.MAX));
        Assert.assertThat(caja.getEfectivo(),is(10.0));
        Assert.assertThat(caja.getTarjeta(),is(200.0));
        Assert.assertThat(caja.getTotalVentaDia(),is(210.0));
        Assert.assertThat(caja.getEfectivoDiaSiguiente(),is(200.0));

    }

}
