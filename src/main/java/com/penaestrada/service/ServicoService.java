package com.penaestrada.service;

import com.penaestrada.dto.CriarServicoDto;

import java.sql.SQLException;

public interface ServicoService {

    void adicionarServico(Long idOrcamento, CriarServicoDto dto) throws SQLException;
}
