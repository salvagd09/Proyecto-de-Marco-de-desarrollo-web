/* 
package com.marcosdeDesarrollo.demo.Entity;

import jakarta.persistence.*;
@Entity
@Table(name = "producto_insumo")
public class Producto_Insumos {
    @EmbeddedId
    private Producto_InsumosId id;
    @Column(name="categoria_requerida",nullable=false)
    private int categoria_requerida;
    @MapsId("id_producto")
    @ManyToOne
    @JoinColumn(name="id_producto",nullable=false)
    private Producto producto;
    @MapsId("id_insumo")
    @ManyToOne
    @JoinColumn(name="id_insumo",nullable=false)
    private Insumos insumos;
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
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Insumos getInsumos() {
        return insumos;
    }

    public void setInsumos(Insumos insumos) {
        this.insumos = insumos;
    }
}
     */