package com.penaestrada.service;

import com.penaestrada.infra.exceptions.ExclusaoVeiculoUnico;
import com.penaestrada.infra.exceptions.VeiculoExistente;
import com.penaestrada.infra.exceptions.VeiculoNotFound;
import com.penaestrada.model.Cliente;
import com.penaestrada.model.Veiculo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface VeiculoService {

    void create(Veiculo veiculo, Connection connection) throws SQLException, VeiculoExistente;
    void adionarVeiculoAoCliente(Veiculo veiculo) throws SQLException, VeiculoExistente;

    List<Veiculo> findVeiculosByClienteId(Long idCliente) throws SQLException;

    void removerVeiculoDoCliente(Cliente cliente, Long id) throws SQLException, VeiculoNotFound, ExclusaoVeiculoUnico;
}
