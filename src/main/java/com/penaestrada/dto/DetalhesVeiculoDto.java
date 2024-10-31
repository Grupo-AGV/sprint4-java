package com.penaestrada.dto;

public record DetalhesVeiculoDto(
        Long id,
        String brand,
        String model,
        String year,
        String licensePlate
) {
}
