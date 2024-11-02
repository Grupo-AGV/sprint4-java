package com.penaestrada.dao;

import com.penaestrada.infra.exceptions.FinalizarOrcamentoSemServico;
import com.penaestrada.infra.exceptions.OrcamentoJaFinalizado;
import com.penaestrada.infra.exceptions.OrcamentoNotFound;
import com.penaestrada.model.Orcamento;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OrcamentoDao {


    void agendarOrcamento(Orcamento orcamento, Connection connection) throws SQLException;

    Orcamento findByIdEUsuario(Usuario usuario, Long idOrcamento, Connection connection) throws SQLException, OrcamentoNotFound;

    void finalizarOrcamento(Usuario usuario, Long id) throws OrcamentoNotFound, FinalizarOrcamentoSemServico, SQLException, OrcamentoJaFinalizado;

    void verificarOrcamentoFinalizado(Long id) throws SQLException, OrcamentoJaFinalizado;

    void verificarSeOrcamentoDoUsuario(Usuario usuario, Long idOrcamento) throws SQLException, OrcamentoNotFound;

    List<Orcamento> findByUsuario(Usuario usuario, Connection connection) throws SQLException;
}
