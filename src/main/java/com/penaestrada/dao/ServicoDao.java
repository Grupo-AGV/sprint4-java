package com.penaestrada.dao;

import com.penaestrada.model.Servico;

import java.sql.SQLException;

public interface ServicoDao {

    void criarServico(Long idOrcamento, Servico servico) throws SQLException;
}
