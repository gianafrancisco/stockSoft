package yporque.model;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;


/**
 * Created by francisco on 12/12/2015.
 */
public class VendedorTest {

    @Test
    public void test_Vendedor_can_instance() throws Exception {

        Vendedor v = new Vendedor("username","password","nombre","apellido");

        Assert.assertThat(v.getVendedorId(),nullValue());
        Assert.assertThat(v.getUsername(),is("username"));
        Assert.assertThat(v.getPassword(),is("password"));
        Assert.assertThat(v.getNombre(),is("nombre"));
        Assert.assertThat(v.getApellido(),is("apellido"));

    }

    @Test
    public void test_Vendedor_setter_can_use() throws Exception {

        Vendedor v = new Vendedor("username","password","nombre","apellido");

        v.setApellido("apellido1");
        v.setNombre("nombre1");
        v.setPassword("password1");
        v.setUsername("username1");

        Assert.assertThat(v.getVendedorId(),nullValue());
        Assert.assertThat(v.getUsername(),is("username1"));
        Assert.assertThat(v.getPassword(),is("password1"));
        Assert.assertThat(v.getNombre(),is("nombre1"));
        Assert.assertThat(v.getApellido(),is("apellido1"));

    }

    @Test
    public void test_Vendedor_toString() throws Exception {

        Vendedor v = new Vendedor("username","password","nombre","apellido");
        String toString = "[ id=null, nombre=nombre, apellido=apellido, username=username, password=password ]";
        Assert.assertThat(v.toString(),is(toString));

    }

}
