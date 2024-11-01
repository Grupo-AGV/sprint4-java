package com.penaestrada.service;

import com.penaestrada.dto.ClienteDashboardDto;
import com.penaestrada.dto.DetalhesClienteOrcamentoDto;
import com.penaestrada.dto.IniciarChatBot;
import com.penaestrada.infra.exceptions.*;
import com.penaestrada.model.Cliente;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;

public interface ClienteService {

    void create(Cliente cliente) throws SQLException, CpfExistente, CpfInvalido, EmailExistente, VeiculoExistente;

    ClienteDashboardDto dashboard(String login) throws ClienteNotFound, SQLException, CpfInvalido;

    DetalhesClienteOrcamentoDto detalhesOrcamentoCliente(Usuario usuario, Connection connection) throws SQLException, CpfInvalido, ClienteNotFound;

    IniciarChatBot iniciarChatBot(Cliente cliente) throws ClienteNotFound, SQLException, CpfInvalido;
}
