package com.marcosdeDesarrollo.Ropa.Domain.Service;

import com.marcosdeDesarrollo.Ropa.Domain.DTO.CategoriaDto;
import com.marcosdeDesarrollo.Ropa.Domain.DTO.CategoriaRequestDto;
import com.marcosdeDesarrollo.Ropa.Persistencia.Entity.Categoria;
import com.marcosdeDesarrollo.Ropa.Domain.Repository.CategoriaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<CategoriaDto> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<CategoriaDto> obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id).map(this::mapToDto);
    }

    public CategoriaDto guardarCategoria(CategoriaRequestDto categoriaRequest) {
        if (categoriaRequest == null) {
            throw new IllegalArgumentException("Los datos de la categoría son obligatorios");
        }

        if (categoriaRequest.getNombreCategoria() == null || categoriaRequest.getNombreCategoria().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }

        Categoria categoria = categoriaRequest.getIdCategoria() != null
                ? categoriaRepository.findById(categoriaRequest.getIdCategoria())
                        .orElseThrow(() -> new IllegalArgumentException("La categoría indicada no existe"))
                : new Categoria();

        categoria.setNombreCategoria(categoriaRequest.getNombreCategoria().trim());
        categoria.setDescripcion(categoriaRequest.getDescripcion());
        categoria.setEstado(categoriaRequest.getEstado());

        Categoria guardada = categoriaRepository.save(categoria);
        return mapToDto(guardada);
    }

    public void eliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }

    public boolean existeCategoria(Long id) {
        return categoriaRepository.existsById(id);
    }

    private CategoriaDto mapToDto(Categoria categoria) {
        CategoriaDto dto = new CategoriaDto();
        dto.setIdCategoria(categoria.getIdCategoria());
        dto.setNombreCategoria(categoria.getNombreCategoria());
        dto.setDescripcion(categoria.getDescripcion());
        dto.setEstado(categoria.getEstado());
        dto.setFechaCreacion(categoria.getFechaCreacion());
        return dto;
    }
}
