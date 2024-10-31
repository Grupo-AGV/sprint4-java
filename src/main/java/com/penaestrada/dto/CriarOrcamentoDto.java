package com.penaestrada.dto;

public record CriarOrcamentoDto(
        Long workshopId,
        Long vehicleId,
        String description,
        String scheduledAt
) {
}
