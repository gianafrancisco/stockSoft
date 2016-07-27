package fransis.mpm.model;

/**
 * Created by francisco on 27/07/2016.
 */
public class ArticuloWebModel {

    private Articulo articulo;
    private Long stockFisico;
    private Long stockVirtual;

    public ArticuloWebModel(Articulo articulo, Long stockFisico, Long stockVirtual) {
        this.articulo = articulo;
        this.stockFisico = stockFisico;
        this.stockVirtual = stockVirtual;
    }

    public Long getArticuloId(){return articulo.getArticuloId();}
    public String getDescripcion(){return articulo.getDescripcion();}
    public String getCodigo(){return articulo.getCodigo();}

    public Long getStockFisico() {
        return stockFisico;
    }

    public Long getStockVirtual() {
        return stockVirtual;
    }
}
