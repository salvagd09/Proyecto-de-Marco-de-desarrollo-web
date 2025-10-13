/* 
package com.marcosdeDesarrollo.demo.Service;

import com.marcosdeDesarrollo.demo.Entity.Venta;
import com.marcosdeDesarrollo.demo.Repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;

    public VentaServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta obtenerVentaPorId(Integer id) {
        return ventaRepository.findById(id).orElse(null);
    }

    @Override
    public Venta guardarVenta(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public Venta actualizarVenta(Integer id, Venta venta) {
        if (ventaRepository.existsById(id)) {
            venta.setIdVenta(id);
            return ventaRepository.save(venta);
        }
        return null;
    }

    @Override
    public void eliminarVenta(Integer id) {
        ventaRepository.deleteById(id);
    }
}

        */