package yporque.model;

import javax.persistence.*;
import java.time.Instant;

/**
 * Created by francisco on 13/12/2015.
 */
@Entity
@Table(name="resumen")
public class Resumen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resumen_id", unique = true, nullable = false)
    private Long resumenId;
    private final Instant fecha;
    @Column(name = "tipo_pago")
    private final TipoDePago tipoPago;
    private final Double efectivo;
    private final Double tarjeta;

    public Resumen() {
        this.fecha = Instant.EPOCH;
        this.tipoPago = TipoDePago.EFECTIVO;
        this.efectivo = 0.0;
        this.tarjeta = 0.0;
    }

    public Resumen(Instant fecha, TipoDePago tipoPago, Double efectivo, Double tarjeta) {
        this.fecha = fecha;
        this.tipoPago = tipoPago;
        this.efectivo = efectivo;
        this.tarjeta = tarjeta;
    }

    public Instant getFecha() {
        return fecha;
    }

    public TipoDePago getTipoPago() {
        return tipoPago;
    }

    public Double getEfectivo() {
        return efectivo;
    }

    public Double getTarjeta() {
        return tarjeta;
    }
}
