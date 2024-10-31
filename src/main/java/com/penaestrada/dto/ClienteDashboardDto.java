package com.penaestrada.dto;

import java.util.List;

public record ClienteDashboardDto(
        Long id,
        String name,
        String email,
        String cpf,
        String birthDate,
        List<DetalhesVeiculoDto> vehicles,
        List<DetalhesTelefoneDto> contacts
) {
}
