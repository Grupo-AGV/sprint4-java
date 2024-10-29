package com.penaestrada.service;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dao.ClienteDao;
import com.penaestrada.dao.ClienteDaoImpl;
import com.penaestrada.infra.exceptions.CpfExistente;
import com.penaestrada.infra.exceptions.CpfInvalido;
import com.penaestrada.model.Cliente;

import java.sql.Connection;
import java.sql.SQLException;

class ClienteServiceImpl implements ClienteService {

    private final ClienteDao dao = new ClienteDaoImpl();

    @Override
    public void create(Cliente cliente, Connection connection) throws SQLException, CpfExistente {
        if (dao.existsByCpf(cliente.getCpf(), connection)) {
            throw new CpfExistente("Cliente já cadastrado com esse CPF.");
        }
        dao.create(cliente, connection);
    }
}
