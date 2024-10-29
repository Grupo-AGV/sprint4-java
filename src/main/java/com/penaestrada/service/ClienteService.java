package com.penaestrada.service;

import com.penaestrada.infra.exceptions.CpfExistente;
import com.penaestrada.infra.exceptions.CpfInvalido;
import com.penaestrada.model.Cliente;

import java.sql.Connection;
import java.sql.SQLException;

public interface ClienteService {

    void create(Cliente cliente, Connection connection) throws SQLException, CpfExistente, CpfInvalido;
}
