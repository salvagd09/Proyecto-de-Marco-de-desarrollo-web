
package com.marcosdeDesarrollo.Ropa.Persistencia.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "detalle_orden")
public class DetalleOrden {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_detalle")
    private Integer idDetalle;
    @Column(name="id_orden")
    private Integer idOrden;
    @Column(name="id_producto")
    private Integer idProducto;
    @ManyToOne
    @JoinColumn(name="id_orden",nullable=false,insertable=false,updatable=false)
    private Ordenes_Compra ordenesCompras;
    @ManyToOne
    @JoinColumn(name="id_producto",nullable=false,insertable=false,updatable=false)
    private Producto productos;
    @Column(name="cantidad",nullable=false)
    private Double cantidad;
    private Double precio_unitario;
    public Integer getId_detalle() {
        return idDetalle;
    }

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Integer getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Integer idOrden) {
        this.idOrden = idOrden;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public void setId_detalle(Integer id_detalle) {
        this.idDetalle = id_detalle;
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

    public Double getCantidad() {
        return cantidad;
    }
    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }
    public Double getPrecio_unitario() {
        return precio_unitario;
    }
    public void setPrecio_unitario(Double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }
}
