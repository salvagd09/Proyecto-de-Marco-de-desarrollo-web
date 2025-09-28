/* 
package com.marcosdeDesarrollo.demo.Entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="gastos")
public class Gastos {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_gasto")
    private int id_gasto;
    @ManyToOne
    @JoinColumn(name="id_usuario",nullable=false)
    private int id_usuario;
    @ManyToOne
    @JoinColumn(name="id_gasto",nullable=false)
    private int id_tipo;
    private String descripcion;
    @Column(name="fecha",nullable=false)
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name="monto",nullable=false)
    private double monto;
    @Enumerated(EnumType.STRING)
    private estado estado;
    private enum estado{
        Pendiente,Pagado,Cancelado
    }

    public int getId_gasto() {
        return id_gasto;
    }

    public void setId_gasto(int id_gasto) {
        this.id_gasto = id_gasto;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public estado getEstado() {
        return estado;
    }

    public void setEstado(estado estado) {
        this.estado = estado;
    }
}
    
*/