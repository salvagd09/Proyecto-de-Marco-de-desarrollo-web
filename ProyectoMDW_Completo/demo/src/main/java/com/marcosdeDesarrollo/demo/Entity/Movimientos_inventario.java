
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
    private Integer idMovimiento;
    @Column(name="id_producto")
    private Integer idProducto;
    @Column(name="id_usuario")
    private Integer idUsuario;
    @ManyToOne
    @JoinColumn(name="id_producto",nullable=false,insertable=false,updatable = false)
    private Producto ProductoMI;
    @ManyToOne
    @JoinColumn(name="id_usuario",nullable=false,insertable=false,updatable=false)
    private Usuario usuarioMI;
    @Temporal(TemporalType.DATE)
    @Value("{fecha:current_timestamp}")
    private Date fecha;
    private String referencia;
    @Column(name="cantidad",nullable=false)
    private Integer cantidad;
    private Integer stock_anterior;
    private Integer stock_nuevo;
    @Enumerated(EnumType.STRING)
    private tipo tipo;
    private enum tipo{
        Entrada,Salida
    }

    public Integer getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Usuario getUsuarioMI() {
        return usuarioMI;
    }

    public void setUsuarioMI(Usuario usuarioMI) {
        this.usuarioMI = usuarioMI;
    }

    public Producto getProductoMI() {
        return ProductoMI;
    }

    public void setProductoMI(Producto productoMI) {
        ProductoMI = productoMI;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getStock_anterior() {
        return stock_anterior;
    }

    public void setStock_anterior(Integer stock_anterior) {
        this.stock_anterior = stock_anterior;
    }

    public Integer getStock_nuevo() {
        return stock_nuevo;
    }

    public void setStock_nuevo(Integer stock_nuevo) {
        this.stock_nuevo = stock_nuevo;
    }

    public tipo getTipo() {
        return tipo;
    }

    public void setTipo(tipo tipo) {
        this.tipo = tipo;
    }
}
