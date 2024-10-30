package com.penaestrada.dto;

public record CriarEndereco(
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
