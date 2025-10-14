
package com.marcosdeDesarrollo.Ropa.Persistencia.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
@Embeddable
public class Producto_InsumosId implements Serializable {
    @Column(name= "id_producto")
    private int idProducto;
    @Column(name= "id_insumo")
    private int idInsumo;

    // Constructor vacío (obligatorio)
    public Producto_InsumosId() {}

    public Producto_InsumosId(int idProducto, int idInsumo) {
        this.idProducto = idProducto;
        this.idInsumo = idInsumo;
    }

    // getters y setters

    // equals() y hashCode() son obligatorios para que JPA funcione correctamente
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto_InsumosId)) return false;
        Producto_InsumosId that = (Producto_InsumosId) o;
        return idProducto == that.idProducto &&
                idInsumo == that.idInsumo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto, idInsumo);
    }
}
