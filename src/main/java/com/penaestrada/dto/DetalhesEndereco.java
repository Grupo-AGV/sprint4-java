package com.penaestrada.dto;

public record DetalhesEndereco(
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
