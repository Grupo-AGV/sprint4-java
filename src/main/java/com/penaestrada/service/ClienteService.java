package com.penaestrada.service;

import com.penaestrada.dto.ClienteDashboardDto;
import com.penaestrada.infra.exceptions.*;
import com.penaestrada.model.Cliente;

import java.sql.Connection;
import java.sql.SQLException;

public interface ClienteService {

    void create(Cliente cliente) throws SQLException, CpfExistente, CpfInvalido, EmailExistente, VeiculoExistente;

    ClienteDashboardDto dashboard(String login) throws ClienteNotFound, SQLException, CpfInvalido;
}
