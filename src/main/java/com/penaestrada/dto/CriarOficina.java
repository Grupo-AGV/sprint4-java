package com.penaestrada.dto;

public record CriarOficina(
        String name,
        String legalName,
        CriarEndereco address,
        Double rating,
        String mapsUrl,
        LoginDto login,
        CriarTelefone contact
) {
}
