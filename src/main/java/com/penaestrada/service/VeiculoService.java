package com.penaestrada.service;

import com.penaestrada.infra.exceptions.VeiculoExistente;
import com.penaestrada.model.Veiculo;

import java.sql.Connection;
import java.sql.SQLException;

public interface VeiculoService {

    void create(Veiculo veiculo, Connection connection) throws SQLException, VeiculoExistente;
}
