package com.penaestrada.dao;

import com.penaestrada.model.Veiculo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface VeiculoDao {

    void create(Veiculo veiculo, Connection connection) throws SQLException;

    Boolean existsByPlaca(String placa, Connection connection) throws SQLException;

    void deleteById(Long id, Connection connection) throws SQLException;

    List<Veiculo> findVeiculosByClienteId(Long id) throws SQLException;
}
