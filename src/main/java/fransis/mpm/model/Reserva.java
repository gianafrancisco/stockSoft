/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.model;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by francisco on 6/30/16.
 */
@Entity
public class Reserva {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserva_id", unique = true, nullable = false)
    @Id
    private Long reservaId;
    private String descripcion;
    private String email;
    private String orderCompraCliente;
    private LocalDate fechaReserva;
    private LocalDate fechaCierre;
    @ManyToOne
    private Vendedor vendedor;
    private EstadoReserva estado;

    public Reserva() {
        this.descripcion = "";
        this.email = "";
        this.estado = EstadoReserva.ACTIVA;
    }

    public Reserva(String descripcion, String email, Vendedor vendedor) {
        this.descripcion = descripcion;
        this.email = email;
        this.vendedor = vendedor;
        this.estado = EstadoReserva.ACTIVA;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEmail() {
        return email;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public EstadoReserva getEstado() {
        return estado;
    }
}
