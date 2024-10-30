package com.penaestrada.dto;

public record CriarClienteDto (
        String name,
        String cpf,
        String birthDate,
        LoginDto login,
        CriarVeiculoDto vehicle
) {
}
