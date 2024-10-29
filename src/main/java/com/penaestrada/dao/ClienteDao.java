package com.penaestrada.dao;

import com.penaestrada.model.Cliente;

import java.sql.Connection;
import java.sql.SQLException;

public interface ClienteDao {

    void create(Cliente cliente, Connection connection) throws SQLException;

    Boolean existsByCpf(String cpf, Connection connection) throws SQLException;
}
