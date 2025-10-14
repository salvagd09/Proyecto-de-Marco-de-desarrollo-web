package com.marcosdeDesarrollo.Ropa.Persistencia.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalle;

    @ManyToOne
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;

    @ManyToOne
    @JoinColumn(name="id_producto")
    private Producto productoVendido;

    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;

    // Getters y setters
    public Integer getIdDetalle() {
        return idDetalle;
    }
    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }
    public Venta getVenta() {
        return venta;
    }
    public void setVenta(Venta venta) {
        this.venta = venta;
    }
    public Producto getProducto() {
        return productoVendido;
    }
    public void setProducto(Producto productoVendido) {
        this.productoVendido = productoVendido;
    }

}