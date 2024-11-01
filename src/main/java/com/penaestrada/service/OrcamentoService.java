package com.penaestrada.service;

import com.penaestrada.dto.CriarOrcamentoDto;
import com.penaestrada.dto.DetalhesOrcamentoDto;
import com.penaestrada.infra.exceptions.*;
import com.penaestrada.infra.security.OficinaNotFound;
import com.penaestrada.model.Cliente;
import com.penaestrada.model.Usuario;

import java.sql.SQLException;

public interface OrcamentoService {
    void agendarOrcamento(Cliente cliente, CriarOrcamentoDto dto) throws SQLException, VeiculoNotFound, OficinaNotFound;

    DetalhesOrcamentoDto findByIdEUsuario(Usuario usuario, Long id) throws SQLException, CpfInvalido, OrcamentoNotFound, ClienteNotFound, OficinaNotFound;

    void finalizarOrcamento(Usuario usuario, Long id) throws OrcamentoNotFound, SQLException, FinalizarOrcamentoSemServico, OrcamentoJaFinalizado;
}
