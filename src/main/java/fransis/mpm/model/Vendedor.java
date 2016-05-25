/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.model;

import javax.persistence.*;

/**
 * Created by francisco on 12/12/2015.
 */
@Entity
@Table(name="vendedor")
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendedor_id", unique = true, nullable = false)
    private Long vendedorId;

    private String username;
    private String password;
    private String nombre;
    private String apellido;

    public Vendedor() {
        this.username = "";
        this.nombre = "";
        this.apellido = "";
    }

    public Vendedor(String username, String password, String nombre, String apellido) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Long getVendedorId() {
        return vendedorId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String toString() {
        return "[ id=" + vendedorId +
                ", nombre=" + nombre + ", apellido=" + apellido +
                ", username=" + username + ", password=" + password + " ]";
    }
}
