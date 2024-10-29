package com.penaestrada.dao;

import com.penaestrada.model.Veiculo;

import java.sql.Connection;
import java.sql.SQLException;

public interface VeiculoDao {

    void create(Veiculo veiculo, Connection connection) throws SQLException;

    Boolean existsByPlaca(String placa, Connection connection) throws SQLException;

    void deleteById(Long id, Connection connection) throws SQLException;
}
