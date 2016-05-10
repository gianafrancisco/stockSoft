package yporque.model;

import java.util.List;

/**
 * Created by francisco on 30/12/15.
 */
public class ResumenCaja {

    private Caja caja;
    private List<Venta> ventaList;
    private List<Retiro> retiroList;

    public ResumenCaja(Caja caja, List<Venta> ventaList, List<Retiro> retiroList) {
        this.caja = caja;
        this.ventaList = ventaList;
        this.retiroList = retiroList;
    }

    public Caja getCaja() {
        return caja;
    }

    public List<Venta> getVentaList() {
        return ventaList;
    }

    public List<Retiro> getRetiroList() {
        return retiroList;
    }
}
