package com.marcosdeDesarrollo.demo.Service;

import com.marcosdeDesarrollo.demo.Entity.Venta;
import java.util.List;

public interface VentaService {
    List<Venta> listarVentas();
    Venta obtenerVentaPorId(Integer id);
    Venta guardarVenta(Venta venta);
    Venta actualizarVenta(Integer id, Venta venta);
    void eliminarVenta(Integer id);
}
