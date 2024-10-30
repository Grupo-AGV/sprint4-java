package com.penaestrada.dto;

public record DetalhesVeiculo(
        Long id,
        String brand,
        String model,
        String year,
        String licensePlate
) {
}
