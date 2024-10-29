package com.penaestrada.dto;

public record CriarClienteDto (
        String nome,
        String cpf,
        String dataNascimento,
        LoginDto login,
        CriarVeiculoDto veiculo
) {
}
