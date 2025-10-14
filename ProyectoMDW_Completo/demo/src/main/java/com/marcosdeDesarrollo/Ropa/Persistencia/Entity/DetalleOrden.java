package com.marcosdeDesarrollo.Ropa.Persistencia.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_orden")
public class DetalleOrden {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_detalle")
    private Integer idDetalle;
    @ManyToOne
    @JoinColumn(name="id_orden",nullable=false)
    private Ordenes_Compra idOrden;
    @ManyToOne
    @JoinColumn(name="id_producto",nullable=false)
    private Producto idProducto;
    @Column(nullable=false)
    private double cantidad;
    @Column(name="precio_unitario")
    private double precioUnitario;
    private double subtotal;
    public Integer getIdDetalle() {
        return idDetalle;
    }
    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }
    public Ordenes_Compra getIdOrden() {
        return idOrden;
    }
    public void setIdOrden(Ordenes_Compra idOrden) {
        this.idOrden = idOrden;
    }
    public Producto getIdProducto() {
        return idProducto;
    }
    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }
    public double getCantidad() {
        return cantidad;
    }
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
    public double getPrecioUnitario() {
        return precioUnitario;
    }
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    public double getSubtotal() {
        return subtotal;
    }
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}