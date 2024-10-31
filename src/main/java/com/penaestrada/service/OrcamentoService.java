package com.penaestrada.service;

import com.penaestrada.dto.CriarOrcamentoDto;
import com.penaestrada.dto.DetalhesOrcamentoDto;
import com.penaestrada.infra.exceptions.ClienteNotFound;
import com.penaestrada.infra.exceptions.CpfInvalido;
import com.penaestrada.infra.exceptions.OrcamentoNotFound;
import com.penaestrada.infra.exceptions.VeiculoNotFound;
import com.penaestrada.infra.security.OficinaNotFound;
import com.penaestrada.model.Cliente;
import com.penaestrada.model.Usuario;

import java.sql.SQLException;

public interface OrcamentoService {
    void agendarOrcamento(Cliente cliente, CriarOrcamentoDto dto) throws SQLException, VeiculoNotFound, OficinaNotFound;

    DetalhesOrcamentoDto findByIdEUsuario(Usuario usuario, Long id) throws SQLException, CpfInvalido, OrcamentoNotFound, ClienteNotFound, OficinaNotFound;
}
