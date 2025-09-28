package com.marcosdeDesarrollo.demo.Entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
@Embeddable
public class Producto_InsumosId implements Serializable {
    private int id_producto;
    private int id_insumo;

    // Constructor vacío (obligatorio)
    public Producto_InsumosId() {}

    public Producto_InsumosId(int id_producto, int id_insumo) {
        this.id_producto = id_producto;
        this.id_insumo = id_insumo;
    }

    // getters y setters

    // equals() y hashCode() son obligatorios para que JPA funcione correctamente
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto_InsumosId)) return false;
        Producto_InsumosId that = (Producto_InsumosId) o;
        return id_producto == that.id_producto &&
                id_insumo == that.id_insumo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_producto, id_insumo);
    }
}
