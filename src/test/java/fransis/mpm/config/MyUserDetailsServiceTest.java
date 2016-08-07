/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.config;

import fransis.mpm.model.Vendedor;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import fransis.mpm.repository.VendedorRepository;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
/**
 * Created by francisco on 18/12/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("fransis.mpm")
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
    @Ignore
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
