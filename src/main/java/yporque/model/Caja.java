package yporque.model;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Created by francisco on 30/12/15.
 */
@Entity
@Table(name = "caja")
public class Caja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="caja_id", unique = true, nullable = false)
    private Long cajaId;
    private Instant apertura;
    @Column(name = "apertura_username")
    private String aperturaUsername;
    private Instant cierre;
    @Column(name = "cierre_username")
    private String cierreUsername;
    private Double efectivo;
    private Double tarjeta;
    @Column(name = "total_venta_dia")
    private Double totalVentaDia;
    @Column(name = "efectivo_dia_siguiente")
    private Double efectivoDiaSiguiente;

    public Caja() {
        this.apertura = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        this.cierre = Instant.EPOCH;
        this.efectivo = 0.0;
        this.tarjeta = 0.0;
        this.totalVentaDia = 0.0;
        this.efectivoDiaSiguiente = 0.0;
        this.aperturaUsername = "";
        this.cierreUsername = "";
    }

    public Caja(Instant apertura, String aperturaUsername) {
        this.apertura = apertura;
        this.aperturaUsername = aperturaUsername;
        this.cierreUsername = "";
        this.cierre = Instant.EPOCH;
        this.efectivo = 0.0;
        this.tarjeta = 0.0;
        this.totalVentaDia = 0.0;
        this.efectivoDiaSiguiente = 0.0;
    }

    public Instant getCierre() {
        return cierre;
    }

    public void setCierre(Instant cierre) {
        this.cierre = cierre;
    }

    public Double getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(Double efectivo) {
        this.efectivo = efectivo;
    }

    public Double getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Double tarjeta) {
        this.tarjeta = tarjeta;
    }

    public Double getTotalVentaDia() {
        return totalVentaDia;
    }

    public void setTotalVentaDia(Double totalVentaDia) {
        this.totalVentaDia = totalVentaDia;
    }

    public Double getEfectivoDiaSiguiente() {
        return efectivoDiaSiguiente;
    }

    public void setEfectivoDiaSiguiente(Double efectivoDiaSiguiente) {
        this.efectivoDiaSiguiente = efectivoDiaSiguiente;
    }

    public Instant getApertura() {
        return apertura;
    }

    public String getAperturaUsername() {
        return aperturaUsername;
    }

    public String getCierreUsername() {
        return cierreUsername;
    }

    public void setCierreUsername(String cierreUsername) {
        this.cierreUsername = cierreUsername;
    }

    public Long getCajaId() {
        return cajaId;
    }

}
