package yporque.repository;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yporque.config.MemoryDBConfig;
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
public class RetiroRepositoryTest {

    @Autowired
    private RetiroRepository retiroRepository;

    @After
    public void tearDown() throws Exception {
        retiroRepository.deleteAll();
    }

    @Test
    public void test_insert_new_retiro() throws Exception {
        Retiro retiro = new Retiro(100.0,"efectivo", Instant.parse("2015-12-30T09:50:00Z"), "username1");
        retiroRepository.save(retiro);
        List<Retiro> retiroList = retiroRepository.findAll();

        Assert.assertThat(retiroList,hasSize(1));
        Assert.assertThat(retiroList.get(0).getMonto(),is(100.0));
        Assert.assertThat(retiroList.get(0).getDescripcion(),is("efectivo"));
        Assert.assertThat(retiroList.get(0).getFecha(),is(Instant.parse("2015-12-30T09:50:00Z")));
        Assert.assertThat(retiroList.get(0).getUsername(),is("username1"));

    }

    @Test
    public void test_findByFechaBetween() throws Exception {
        Retiro retiro = new Retiro(100.0,"efectivo", Instant.parse("2015-12-30T09:50:00Z"), "username1");
        retiroRepository.save(retiro);
        List<Retiro> retiroList = retiroRepository.findByFechaBetween(Instant.parse("2015-12-29T00:00:00Z"),Instant.parse("2015-12-30T09:50:00Z"));

        Assert.assertThat(retiroList,hasSize(1));
        Assert.assertThat(retiroList.get(0).getMonto(),is(100.0));
        Assert.assertThat(retiroList.get(0).getDescripcion(),is("efectivo"));
        Assert.assertThat(retiroList.get(0).getFecha(),is(Instant.parse("2015-12-30T09:50:00Z")));
        Assert.assertThat(retiroList.get(0).getUsername(),is("username1"));

    }

    @Test
    public void test_findByFechaBetween_out_range() throws Exception {
        Retiro retiro = new Retiro(100.0,"efectivo", Instant.parse("2015-12-30T09:50:00Z"), "username1");
        retiroRepository.save(retiro);
        List<Retiro> retiroList = retiroRepository.findByFechaBetween(Instant.parse("2015-12-29T00:00:00Z"),Instant.parse("2015-12-29T23:59:59Z"));

        Assert.assertThat(retiroList,hasSize(0));

    }

}
