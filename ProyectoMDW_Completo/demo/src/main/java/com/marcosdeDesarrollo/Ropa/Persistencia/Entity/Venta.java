/* 
package com.marcosdeDesarrollo.demo.Entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVenta;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "id_cliente")
    private Integer idCliente;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    private Double total;

    @Enumerated(EnumType.STRING)
    private EstadoVenta estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago")
    private MetodoPago metodoPago;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles;

    // Getters y setters

    public enum EstadoVenta {
        Pendiente, Pagada, Cancelada
    }

    public enum MetodoPago {
        Efectivo, Tarjeta, Transferencia
    }

    // Getters y setters 
    public Integer getIdVenta() {
        return idVenta;
    }
    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }
    public Integer getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
    public Integer getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public Double getTotal() {
        return total;
    }
    public void setTotal(Double total) {
        this.total = total;
    }
    public EstadoVenta getEstado() {
        return estado;
    }
    public void setEstado(EstadoVenta estado) {
        this.estado = estado;
    }
    public MetodoPago getMetodoPago() {
        return metodoPago;
    }
    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }
    public List<DetalleVenta> getDetalles() {
        return detalles;
    }
    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

}

//     */