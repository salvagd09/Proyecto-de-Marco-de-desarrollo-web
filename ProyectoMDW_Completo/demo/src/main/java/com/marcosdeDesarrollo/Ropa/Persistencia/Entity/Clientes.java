/* 
package com.marcosdeDesarrollo.demo.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="clientes")
public class Clientes {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_cliente")
    private int id_cliente;
    @Column(name="nombre",nullable=false)
    private String nombre;
    private String apellido;
    @Column(name="dni_ruc",unique=true)
    private String dni_ruc;
    private String telefono;
    private String email;

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
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

    public String getDni_ruc() {
        return dni_ruc;
    }

    public void setDni_ruc(String dni_ruc) {
        this.dni_ruc = dni_ruc;
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
}
*/