package com.penaestrada.dao;

import com.penaestrada.infra.exceptions.CpfInvalido;
import com.penaestrada.model.Cliente;
import com.penaestrada.model.Veiculo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ClienteDao {

    void create(Cliente cliente, Connection connection) throws SQLException;

    Boolean existsByCpf(String cpf, Connection connection) throws SQLException;

    Cliente findByLogin(String login, Connection connection) throws CpfInvalido, SQLException;

    List<Veiculo> findVeiculosByCliente(Cliente cliente, Connection connection);
}
