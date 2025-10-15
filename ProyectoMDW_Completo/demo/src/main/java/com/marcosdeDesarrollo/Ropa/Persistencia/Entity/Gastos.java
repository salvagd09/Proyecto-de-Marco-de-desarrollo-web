
package com.marcosdeDesarrollo.Ropa.Persistencia.Entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="gastos")
public class Gastos {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_gasto")
    private Integer idGasto;
    @Column(name="id_usuario")
    private Integer idUsuario;
    @Column(name="id_tipo")
    private Integer idTipo;
    @ManyToOne
    @JoinColumn(name="id_usuario",nullable=false,insertable=false,updatable=false)
    private Usuario usuarioGasto;
    @ManyToOne
    @JoinColumn(name="id_tipo",nullable=false,insertable=false,updatable=false)
    private Tipos_Gasto gasto;
    private String descripcion;
    @Column(name="fecha",nullable=false)
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name="monto",nullable=false)
    private Double monto;
    @Enumerated(EnumType.STRING)
    private estado estado;
    private enum estado{
        Pendiente,Pagado,Cancelado
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Integer idTipo) {
        this.idTipo = idTipo;
    }

    public Usuario getUsuarioGasto() {
        return usuarioGasto;
    }

    public void setUsuarioGasto(Usuario usuarioGasto) {
        this.usuarioGasto = usuarioGasto;
    }
    public Double getMonto() {
        return monto;
    }
    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Integer getIdGasto() {
        return idGasto;
    }

    public void setIdGasto(Integer idGasto) {
        this.idGasto = idGasto;
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

    public estado getEstado() {
        return estado;
    }

    public void setEstado(estado estado) {
        this.estado = estado;
    }
}