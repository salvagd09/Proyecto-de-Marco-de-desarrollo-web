
package com.marcosdeDesarrollo.Ropa.Persistencia.Entity;

import jakarta.persistence.*;
@Entity
@Table(name = "producto_insumo")
public class Producto_Insumos {
    @EmbeddedId
    private Producto_InsumosId id;
    @MapsId("idProducto")
    @ManyToOne
    @JoinColumn(name="id_producto",nullable=false)
    private Producto productoI;
    @MapsId("idInsumo")
    @ManyToOne
    @JoinColumn(name="id_insumo",nullable=false)
    private Insumos insumos;
    @Column(name="categoria_requerida",nullable=false)
    private int categoria_requerida;
    public Producto_InsumosId getId() {
        return id;
    }

    public void setId(Producto_InsumosId id) {
        this.id = id;
    }

    public int getCategoria_requerida() {
        return categoria_requerida;
    }

    public void setCategoria_requerida(int categoria_requerida) {
        this.categoria_requerida = categoria_requerida;
    }

    public Producto getProducto() {
        return productoI;
    }

    public void setProducto(Producto productoI) {
        this.productoI = productoI;
    }

    public Insumos getInsumos() {
        return insumos;
    }

    public void setInsumos(Insumos insumos) {
        this.insumos = insumos;
    }
}
