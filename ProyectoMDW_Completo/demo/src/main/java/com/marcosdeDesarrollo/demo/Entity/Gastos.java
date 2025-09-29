
package com.marcosdeDesarrollo.demo.Entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="gastos")
public class Gastos {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id_gasto;
    @ManyToOne
    @JoinColumn(name="id_usuario",nullable=false)
    private Usuario id_usuario;
    @ManyToOne
    @JoinColumn(name="id_tipo",nullable=false)
    private Tipos_Gasto tipo;
    private String descripcion;
    @Column(name="fecha",nullable=false)
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion", nullable = false, updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date fechaCreacion;
    @Column(name="monto",nullable=false)
    private double monto;
    @Enumerated(EnumType.STRING)
    private Estado2 estado;

    public int getId_gasto() {
        return id_gasto;
    }

    public void setId_gasto(Integer id_gasto) {
        this.id_gasto = id_gasto;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setId_gasto(int id_gasto) {
        this.id_gasto = id_gasto;
    }

    public Usuario getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Usuario id_usuario) {
        this.id_usuario = id_usuario;
    }

        public Tipos_Gasto getId_tipo() {
        return tipo;
    }

    public void setId_tipo(Tipos_Gasto tipo) {
        this.tipo = tipo;
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

    public Estado2 getEstado() {
        return estado;
    }

    public void setEstado(Estado2 estado) {
        this.estado = estado;
    }
}
    
