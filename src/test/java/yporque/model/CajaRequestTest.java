package yporque.model;

import org.junit.Assert;
import org.junit.Test;
import yporque.request.CajaRequest;

import java.time.Instant;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;


/**
 * Created by francisco on 12/12/2015.
 */
public class CajaRequestTest {

    @Test
    public void test_new_CajaRequest() throws Exception {

        CajaRequest cajaRequest = new CajaRequest("usr1", Instant.parse("2015-12-30T00:00:00Z"));

        Assert.assertThat(cajaRequest.getUsername(),is("usr1"));
        Assert.assertThat(cajaRequest.getFecha(),is(Instant.parse("2015-12-30T00:00:00Z")));

    }

    @Test
    public void test_new_CajaRequest_default_constructor() throws Exception {

        CajaRequest cajaRequest = new CajaRequest();

        Assert.assertThat(cajaRequest.getUsername(),is(""));
        Assert.assertThat(cajaRequest.getFecha(),notNullValue());

    }


}
