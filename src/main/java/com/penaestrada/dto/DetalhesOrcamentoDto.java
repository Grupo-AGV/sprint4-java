package com.penaestrada.dto;

import java.util.List;

public record DetalhesOrcamentoDto(
        Long id,
        DetalhesClienteOrcamentoDto client,
        DetalhesVeiculoDto vehicle,
        DetalhesOficinaDto workshop,
        String initialDescription,
        String scheduledAt,
        String createdAt,
        Double value,
        String finishedAt,
        List<DetalhesServicoDto> services
) {
}
