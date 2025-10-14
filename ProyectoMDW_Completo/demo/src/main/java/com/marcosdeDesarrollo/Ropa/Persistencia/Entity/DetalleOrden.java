
package com.marcosdeDesarrollo.Ropa.Persistencia.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "detalle_orden")
public class DetalleOrden {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_detalle")
    private Integer id_detalle;
    @ManyToOne
    @JoinColumn(name="id_orden")
    private Ordenes_Compra ordenesCompras;
    @ManyToOne
    @JoinColumn(name="id_producto")
    private Producto productos;

    @Column(name="cantidad",nullable=false)
    private double cantidad;
    private double precio_unitario;
    public Integer getId_detalle() {
        return id_detalle;
    }
    public void setId_detalle(Integer id_detalle) {
        this.id_detalle = id_detalle;
    }

    public Ordenes_Compra getOrdenesCompras() {
        return ordenesCompras;
    }

    public void setOrdenesCompras(Ordenes_Compra ordenesCompras) {
        this.ordenesCompras = ordenesCompras;
    }

    public Producto getProductos() {
        return productos;
    }

    public void setProductos(Producto productos) {
        this.productos = productos;
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
