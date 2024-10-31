package com.penaestrada.service;

import com.penaestrada.dto.CriarOrcamentoDto;
import com.penaestrada.infra.exceptions.VeiculoNotFound;
import com.penaestrada.infra.security.OficinaNotFound;
import com.penaestrada.model.Cliente;

import java.sql.SQLException;

public interface OrcamentoService {
    void agendarOrcamento(Cliente cliente, CriarOrcamentoDto dto) throws SQLException, VeiculoNotFound, OficinaNotFound;

}
