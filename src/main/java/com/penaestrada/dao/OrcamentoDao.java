package com.penaestrada.dao;

import com.penaestrada.infra.exceptions.OrcamentoNotFound;
import com.penaestrada.model.Orcamento;

import java.sql.Connection;
import java.sql.SQLException;

public interface OrcamentoDao {


    void agendarOrcamento(Orcamento orcamento, Connection connection) throws SQLException;

    Orcamento findByIdEDonoVeiculo(Long idDonoVeiculo, Long idOrcamento, Connection connection) throws SQLException, OrcamentoNotFound;
}
