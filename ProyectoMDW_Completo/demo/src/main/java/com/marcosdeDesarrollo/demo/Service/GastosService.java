package com.marcosdeDesarrollo.demo.Service;

import com.marcosdeDesarrollo.demo.Entity.Gastos;

import java.util.List;

public interface GastosService {
    List<Gastos> listarGastos();
    Gastos obtenerGastosPorId(Integer id);
    Gastos guardarGasto(Gastos gasto);
    Gastos actualizarGasto(Integer id, Gastos gasto);
    void eliminarGasto(Integer id);
}
