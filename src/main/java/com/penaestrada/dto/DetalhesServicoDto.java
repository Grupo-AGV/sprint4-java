package com.penaestrada.dto;

public record DetalhesServicoDto(
        Long id,
        String description,
        Double price,
        String createdAt
) {
}
