package yporque.model;

import org.junit.Assert;
import org.junit.Test;
import yporque.request.VentaRequest;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;


/**
 * Created by francisco on 12/12/2015.
 */
public class VentaRequestTest {

    @Test
    public void test_new_VentaRequest() throws Exception {
        Articulo articulo = new Articulo("1234","articulo 1",1.0,2.0,2.0,10,10);
        VentaRequest ventaRequest = new VentaRequest(articulo,10,new Vendedor("username1","1234","nombre1","apellido1"),"Efectivo", "cupon1", 0.0);

        Assert.assertThat(ventaRequest.getCantidad(),is(10));
        Assert.assertThat(ventaRequest.getArticulo().getCodigo(),is("1234"));
        Assert.assertThat(ventaRequest.getArticulo().getDescripcion(),is("articulo 1"));
        Assert.assertThat(ventaRequest.getFormaPago(),is("Efectivo"));
        Assert.assertThat(ventaRequest.getVendedor().getUsername(),is("username1"));
        Assert.assertThat(ventaRequest.getVendedor().getNombre(),is("nombre1"));
        Assert.assertThat(ventaRequest.getVendedor().getApellido(),is("apellido1"));
        Assert.assertThat(ventaRequest.getVendedor().getPassword(),is("1234"));
        Assert.assertThat(ventaRequest.getNroCupon(),is("cupon1"));

    }

    @Test
    public void test_new_VentaRequest_default_constructor() throws Exception {

        VentaRequest ventaRequest = new VentaRequest();

        Assert.assertThat(ventaRequest.getCantidad(),is(0));
        Assert.assertThat(ventaRequest.getArticulo(),nullValue());
        Assert.assertThat(ventaRequest.getFormaPago(),is("Efectivo"));
        Assert.assertThat(ventaRequest.getVendedor(),nullValue());
        Assert.assertThat(ventaRequest.getNroCupon(),is(""));

    }


}
