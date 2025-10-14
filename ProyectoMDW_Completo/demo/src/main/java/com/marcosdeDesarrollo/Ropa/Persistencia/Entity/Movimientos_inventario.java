
package com.marcosdeDesarrollo.Ropa.Persistencia.Entity;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Entity
@Table(name="movimientos_inventario")
public class Movimientos_inventario {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_movimiento")
    private Integer id_movimiento;
    @ManyToOne
    @JoinColumn(name="id_producto",nullable=false)
    private Producto ProductoMI;
    @ManyToOne
    @JoinColumn(name="id_usuario",nullable=false)
    private Usuario usuarioMI;
    @Temporal(TemporalType.DATE)
    @Value("{fecha:current_timestamp}")
    private Date fecha;
    @Enumerated(EnumType.STRING)
    private tipo tipo;
    private enum tipo{
        Entrada,Salida
    }
    private String referencia;
    @Column(name="cantidad",nullable=false)
    private int cantidad;
    private int stock_anterior;
    private int stock_nuevo;

    public Integer getId_movimiento() {
        return id_movimiento;
    }

    public void setId_movimiento(Integer id_movimiento) {
        this.id_movimiento = id_movimiento;
    }

    public Producto getProductoMI() {
        return ProductoMI;
    }

    public void setProductoMI(Producto productoMI) {
        ProductoMI = productoMI;
    }

    public Usuario getUsuarioMI() {
        return usuarioMI;
    }

    public void setUsuarioMI(Usuario usuarioMI) {
        this.usuarioMI = usuarioMI;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public tipo getTipo() {
        return tipo;
    }

    public void setTipo(tipo tipo) {
        this.tipo = tipo;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getStock_anterior() {
        return stock_anterior;
    }

    public void setStock_anterior(int stock_anterior) {
        this.stock_anterior = stock_anterior;
    }

    public int getStock_nuevo() {
        return stock_nuevo;
    }

    public void setStock_nuevo(int stock_nuevo) {
        this.stock_nuevo = stock_nuevo;
    }
}
