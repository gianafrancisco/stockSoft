package yporque.repository;

/**
 * Created by francisco on 12/12/2015.
 */

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yporque.config.MemoryDBConfig;
import yporque.model.*;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

/**
 * Created by francisco on 04/12/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("yporque.model")
@ContextConfiguration(classes = {MemoryDBConfig.class})
public class VendedorRepositoryTest {

    @Autowired
    private VendedorRepository vendedorRepository;

    @After
    public void tearDown() throws Exception {
        vendedorRepository.deleteAll();
    }

    @Test
    public void test_insert_vendedor() throws Exception {
        Vendedor v = new Vendedor("username","password","nombre","apellido");
        vendedorRepository.save(v);
        Vendedor v1 = vendedorRepository.findOne(v.getVendedorId());
        Assert.assertThat(v1.toString(),is(v.toString()));
    }

    @Test
    public void test_find_vendedor_by_username() throws Exception {
        Vendedor v = new Vendedor("username1","password1","nombre1","apellido1");
        vendedorRepository.save(v);
        List<Vendedor> list = vendedorRepository.findByUsername("username1");
        Assert.assertThat(list,hasSize(1));
        Assert.assertThat(list.get(0).toString(),is(v.toString()));
    }

    @Test
    public void test_find_vendedor_by_username_or_nombre_or_apellido() throws Exception {
        Vendedor v = new Vendedor("username1","password1","nombre1","apellido1");
        vendedorRepository.save(v);
        Page<Vendedor> list = vendedorRepository.findByUsernameContainingIgnoreCaseOrNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase("Sername","Sername","Sername",new PageRequest(0,10));
        Assert.assertThat(list.getContent(),hasSize(1));
        Assert.assertThat(list.getContent().get(0).toString(),is(v.toString()));

        list = vendedorRepository.findByUsernameContainingIgnoreCaseOrNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase("OMB","OMB","OMB",new PageRequest(0,10));
        Assert.assertThat(list.getContent(),hasSize(1));
        Assert.assertThat(list.getContent().get(0).toString(),is(v.toString()));

        list = vendedorRepository.findByUsernameContainingIgnoreCaseOrNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase("PELL","PELL","PELL",new PageRequest(0,10));
        Assert.assertThat(list.getContent(),hasSize(1));
        Assert.assertThat(list.getContent().get(0).toString(),is(v.toString()));


    }


}


