package yporque.config;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yporque.model.Vendedor;
import yporque.repository.VendedorRepository;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
/**
 * Created by francisco on 18/12/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("yporque")
@ContextConfiguration(classes = {MemoryDBConfig.class,MyUserDetailsService.class})
public class MyUserDetailsServiceTest {

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Before
    public void setUp() throws Exception {
        vendedorRepository.save(new Vendedor("Administrador","123456","Admin","Admin"));
        vendedorRepository.save(new Vendedor("username1","123456","Admin","Admin"));
    }

    @After
    public void tearDown() throws Exception {
        vendedorRepository.deleteAll();
    }


    @Test
    public void test_loadUserByUsername() throws Exception {

        UserDetails userDetails = myUserDetailsService.loadUserByUsername("Administrador");

        Assert.assertThat(userDetails.getUsername(),is("Administrador"));
        Assert.assertThat(userDetails.getPassword(),is("123456"));


    }

    @Test
    public void test_loadUserByUsername_not_administrador() throws Exception {

        UserDetails userDetails = myUserDetailsService.loadUserByUsername("username1");

        Assert.assertThat(userDetails,nullValue());

    }

    @Test(expected = RuntimeException.class)
    public void test_loadUserByUsername_not_exists() throws Exception {

        UserDetails userDetails = myUserDetailsService.loadUserByUsername("username_not_exist");

        Assert.assertThat(userDetails,nullValue());

    }
}
