package com.marcosdeDesarrollo.Ropa.Domain.DTO;

import jakarta.validation.constraints.NotBlank;

// Un 'record' es una forma moderna y corta de crear una clase DTO inmutable.
// Automáticamente crea los campos, el constructor y los métodos 'email()' y 'password()'.
// Añadimos @NotBlank para asegurar que los campos no vengan vacíos.
public record LoginRequest(
        @NotBlank String email,
        @NotBlank String password
) {
}