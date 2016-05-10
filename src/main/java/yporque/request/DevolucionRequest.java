package yporque.request;

import yporque.model.Vendedor;
import yporque.model.Venta;

/**
 * Created by francisco on 23/12/15.
 */
public class DevolucionRequest {
    private final Venta venta;
    private final Integer cantidad;
    private final Vendedor vendedor;
    private final String formaPago;
    private final String nroCupon;

    public DevolucionRequest() {
        this.formaPago = "Efectivo";
        this.cantidad = 0;
        this.venta = null;
        this.vendedor = null;
        this.nroCupon = "";
    }

    public DevolucionRequest(Venta venta, Integer cantidad, Vendedor vendedor, String formaPago, String nroCupon) {
        this.venta = venta;
        this.cantidad = cantidad;
        this.vendedor = vendedor;
        this.formaPago = formaPago;
        this.nroCupon = nroCupon;
    }

    public Venta getVenta() {
        return venta;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public String getNroCupon() {
        return nroCupon;
    }
}
