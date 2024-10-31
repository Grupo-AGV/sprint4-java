package com.penaestrada.dto;

public record CriarOficinaDto(
        String name,
        String legalName,
        CriarEnderecoDto address,
        Double rating,
        String mapsUrl,
        LoginDto login,
        CriarTelefoneDto contact
) {
}
