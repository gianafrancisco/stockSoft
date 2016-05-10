package yporque.model;

import javax.persistence.*;

/**
 * Created by francisco on 21/11/15.
 */
@Entity
@Table(name="articulo")
public class Articulo {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "articulo_id", unique = true, nullable = false)
    @Id
    private Long articuloId;
    @Column(name = "precio_lista")
    private final Double precioLista;
    private final Double factor1;
    private final Double factor2;
    private String descripcion;
    @Transient
    private Integer cantidad;
    @Column(name = "cantidad_stock")
    private Integer cantidadStock;
    private final String codigo;


    public Articulo() {
        this.precioLista = 0.0;
        this.factor1 = 1.0;
        this.factor2 = 1.0;
        this.descripcion = "";
        this.codigo = "";
        this.cantidad = 0;
        this.cantidadStock = 0;
    }

    public Articulo(String codigo, String descripcion, Double precioLista, Double factor1, Double factor2, Integer cantidad, Integer cantidadStock) {
        this.codigo = codigo;
        this.precioLista = precioLista;
        this.factor1 = factor1;
        this.factor2 = factor2;
        this.descripcion = descripcion;
        this.cantidadStock = cantidadStock;
        this.cantidad = cantidad;
    }

    public Long getArticuloId() {
        return articuloId;
    }

    public Double getPrecio() {
        return actualizarPrecio();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    private Double actualizarPrecio(){
        return Math.ceil(precioLista*factor1*factor2*10)/10;
    }

    public Integer getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(Integer cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    public String getCodigo() {
        return codigo;
    }

    public Double getPrecioLista() {
        return precioLista;
    }

    public Double getFactor1() {
        return factor1;
    }

    public Double getFactor2() {
        return factor2;
    }

    public Integer getCantidad() {
        return cantidad;
    }
}

