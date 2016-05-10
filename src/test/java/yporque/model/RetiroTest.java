package yporque.model;

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

import static org.hamcrest.core.Is.is;

/**
 * Created by francisco on 13/12/2015.
 */
public class RetiroTest {

    @Test
    public void test_new_retiro() throws Exception {
        Retiro retiro = new Retiro(100.0,"efectivo", Instant.parse("2015-12-30T09:50:00Z"), "username1");

        Assert.assertThat(retiro.getDescripcion(),is("efectivo"));
        Assert.assertThat(retiro.getMonto(),is(100.0));
        Assert.assertThat(retiro.getFecha(),is(Instant.parse("2015-12-30T09:50:00Z")));
        Assert.assertThat(retiro.getUsername(),is("username1"));
    }

    @Test
    public void test_default_constructor() throws Exception {

        Retiro retiro = new Retiro();

        Assert.assertThat(retiro.getDescripcion(),is(""));
        Assert.assertThat(retiro.getMonto(),is(0.0));
        Assert.assertThat(retiro.getFecha(),is(Instant.EPOCH));
        Assert.assertThat(retiro.getUsername(),is(""));
    }
}
