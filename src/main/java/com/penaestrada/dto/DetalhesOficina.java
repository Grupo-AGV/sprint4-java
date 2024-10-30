package com.penaestrada.dto;

import java.util.List;

public record DetalhesOficina(
        Long id,
        String name,
        List<DetalhesEndereco> addresses,
        Double rating,
        String mapsUrl,
        List<DetalhesTelefone> contacts
) {
}
