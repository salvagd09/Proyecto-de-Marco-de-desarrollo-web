package com.marcosdeDesarrollo.demo.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_orden")
public class DetalleOrden {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_detalle")
    private Integer id_detalle;
    @ManyToOne
    @JoinColumn(name="id_orden",nullable=false)
    private Integer id_orden;
    @ManyToOne
    @JoinColumn(name="id_producto",nullable=false)
    private Integer id_producto;
    @Column(name="cantidad",nullable=false)
    private double cantidad;
    private double precio_unitario;
    public Integer getId_detalle() {
        return id_detalle;
    }
    public void setId_detalle(Integer id_detalle) {
        this.id_detalle = id_detalle;
    }
    public Integer getId_orden() {
        return id_orden;
    }
    public void setId_orden(Integer id_orden) {
        this.id_orden = id_orden;
    }
    public Integer getId_producto() {
        return id_producto;
    }
    public void setId_producto(Integer id_producto) {
        this.id_producto = id_producto;
    }
    public double getCantidad() {
        return cantidad;
    }
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
    public double getPrecio_unitario() {
        return precio_unitario;
    }
    public void setPrecio_unitario(double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }
}
