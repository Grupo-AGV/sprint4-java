package com.penaestrada.dto;

public record DetalhesEnderecoDto(
        Long id,
        String streetName,
        String number,
        String referencePoint,
        String zipCode,
        String neighborhood,
        String neighborhoodZone,
        String city,
        String state
) {
}
