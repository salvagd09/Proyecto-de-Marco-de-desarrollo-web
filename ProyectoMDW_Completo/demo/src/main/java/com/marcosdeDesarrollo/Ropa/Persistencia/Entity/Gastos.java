
package com.marcosdeDesarrollo.Ropa.Persistencia.Entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="gastos")
public class Gastos {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_gasto",insertable=false,updatable=false)
    private Integer idGasto;
    @ManyToOne
    @JoinColumn(name="id_usuario",nullable=false)
    private Usuario usuarioGasto;
    @ManyToOne
    @JoinColumn(name="id_gasto",nullable=false)
    private Tipos_Gasto gasto;
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

    public Integer getIdGasto() {
        return idGasto;
    }

    public void setIdGasto(Integer idGasto) {
        this.idGasto = idGasto;
    }

    public Usuario getUsuario() {
        return usuarioGasto;
    }

    public void setUsuario(Usuario usuarioGasto) {
        this.usuarioGasto = usuarioGasto;
    }

    public Tipos_Gasto getGasto() {
        return gasto;
    }

    public void setGasto(Tipos_Gasto gasto) {
        this.gasto = gasto;
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