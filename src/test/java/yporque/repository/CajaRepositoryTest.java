package yporque.repository;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yporque.config.MemoryDBConfig;
import yporque.model.Caja;
import yporque.model.Retiro;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

/**
 * Created by francisco on 04/12/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("yporque.model")
@ContextConfiguration(classes = {MemoryDBConfig.class})
public class CajaRepositoryTest {

    @Autowired
    private CajaRepository cajaRepository;

    @After
    public void tearDown() throws Exception {
        cajaRepository.deleteAll();
    }

    @Test
    public void test_insert() throws Exception {
        Caja caja = new Caja(Instant.parse("2015-12-30T12:00:00Z"),"username1");

        cajaRepository.save(caja);

        List<Caja> cajaList = cajaRepository.findAll();

        Assert.assertThat(cajaList,hasSize(1));

        Assert.assertThat(caja.getApertura(),is(Instant.parse("2015-12-30T12:00:00Z")));
        Assert.assertThat(caja.getAperturaUsername(),is("username1"));

        Assert.assertThat(caja.getCierreUsername(),is(""));
        Assert.assertThat(caja.getCierre(),is(Instant.EPOCH));
        Assert.assertThat(caja.getEfectivo(),is(0.0));
        Assert.assertThat(caja.getTarjeta(),is(0.0));
        Assert.assertThat(caja.getTotalVentaDia(),is(0.0));
        Assert.assertThat(caja.getEfectivoDiaSiguiente(),is(0.0));

    }

    @Test
    public void test_findByCierre() throws Exception {
        Caja caja = new Caja(Instant.parse("2015-12-30T12:00:00Z"),"username1");

        cajaRepository.save(caja);

        List<Caja> cajaList = cajaRepository.findByCierre(Instant.EPOCH);

        Assert.assertThat(cajaList,hasSize(1));

        Assert.assertThat(caja.getApertura(),is(Instant.parse("2015-12-30T12:00:00Z")));
        Assert.assertThat(caja.getAperturaUsername(),is("username1"));

        Assert.assertThat(caja.getCierreUsername(),is(""));
        Assert.assertThat(caja.getCierre(),is(Instant.EPOCH));
        Assert.assertThat(caja.getEfectivo(),is(0.0));
        Assert.assertThat(caja.getTarjeta(),is(0.0));
        Assert.assertThat(caja.getTotalVentaDia(),is(0.0));
        Assert.assertThat(caja.getEfectivoDiaSiguiente(),is(0.0));

    }

}
