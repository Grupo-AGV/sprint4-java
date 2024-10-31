package com.penaestrada.dto;

import java.util.List;

public record DetalhesOficinaDto(
        Long id,
        String name,
        List<DetalhesEnderecoDto> addresses,
        Double rating,
        String mapsUrl,
        List<DetalhesTelefoneDto> contacts
) {
}
