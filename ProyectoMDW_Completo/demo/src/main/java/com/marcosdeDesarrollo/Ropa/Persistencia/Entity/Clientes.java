
package com.marcosdeDesarrollo.Ropa.Persistencia.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="clientes")
public class Clientes {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_cliente",insertable=false,updatable=false)
    private int idCliente;
    @Column(nullable=false)
    private String nombre;
    private String apellido;
    @Column(unique=true)
    private String dniRuc;
    private String telefono;
    private String email;
    @OneToMany(mappedBy="cliente")
    private List<Venta> ventas;
    public int getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDniRuc() {
        return dniRuc;
    }
    public void setDniRuc(String dniRuc) {
        this.dniRuc = dniRuc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public List<Venta> getVentas() {
        return ventas;
    }
    public void setVentas(List<Venta> ventas) {
        this.ventas = ventas;
    }
}
