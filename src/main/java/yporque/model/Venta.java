package yporque.model;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Created by francisco on 13/12/2015.
 */
@Entity
@Table(name="venta",
        indexes = {@Index(name = "fecha",  columnList="fecha", unique = false)})
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venta_id", unique = true, nullable = false)
    private Long ventaId;
    private final Instant fecha;
    private final String codigo;
    private final String descripcion;
    private final Integer cantidad;
    private final Double factor1;
    private final Double factor2;
    @Column(name = "precio_lista")
    private final Double precioLista;
    private final Double precio;
    @Column(name = "tipo_pago")
    private final TipoDePago tipoPago;
    private final String username;
    @Column(name = "nro_cupon")
    private final String nroCupon;
    @Column(name = "codigo_devolucion")
    private final String codigoDevolucion;
    private Boolean devuelto;

    public Venta() {
        this.codigo = "";
        this.descripcion = "";
        this.fecha = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        this.factor1 = 1.0;
        this.factor2 = 1.0;
        this.tipoPago = TipoDePago.EFECTIVO;
        this.precio = 0.0;
        this.precioLista = 0.0;
        this.username = "";
        this.cantidad = 0;
        this.nroCupon = "";
        this.codigoDevolucion = String.format("%x",this.fecha.getEpochSecond());
        this.devuelto = false;
    }

    public Venta(Instant fecha, String codigo, String descripcion, Integer cantidad, Double factor1, Double factor2, Double precioLista, Double precio, TipoDePago tipoPago, String username, String nroCupon) {
        this.fecha = fecha;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.factor1 = factor1;
        this.factor2 = factor2;
        this.precioLista = precioLista;
        this.precio = precio;
        this.tipoPago = tipoPago;
        this.username = username;
        this.nroCupon = nroCupon;
        this.codigoDevolucion = String.format("%x",this.fecha.getEpochSecond());
        this.devuelto = false;
    }

    public void setDevuelto(Boolean devuelto) {
        this.devuelto = devuelto;
    }

    public Long getVentaId() {
        return ventaId;
    }

    public Instant getFecha() {
        return fecha;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Double getFactor1() {
        return factor1;
    }

    public Double getFactor2() {
        return factor2;
    }

    public Double getPrecioLista() {
        return precioLista;
    }

    public Double getPrecio() {
        return precio;
    }

    public TipoDePago getTipoPago() {
        return tipoPago;
    }

    public String getUsername() {
        return username;
    }

    public String getNroCupon() {
        return nroCupon;
    }

    public String getCodigoDevolucion() {
        return codigoDevolucion;
    }
}
