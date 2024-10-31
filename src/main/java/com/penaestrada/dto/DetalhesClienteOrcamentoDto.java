package com.penaestrada.dto;

import java.util.List;

public record DetalhesClienteOrcamentoDto(
        Long id,
        String name,
        String email,
        List<DetalhesTelefoneDto> contatcs
) {
}
