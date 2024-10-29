package com.penaestrada.service;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dao.VeiculoDao;
import com.penaestrada.dao.VeiculoDaoImpl;
import com.penaestrada.infra.exceptions.VeiculoExistente;
import com.penaestrada.model.Veiculo;

import java.sql.Connection;
import java.sql.SQLException;

class VeiculoServiceImpl implements VeiculoService {

    private final VeiculoDao dao = new VeiculoDaoImpl();

    @Override
    public void create(Veiculo veiculo, Connection connection) throws SQLException, VeiculoExistente {
        veiculo.setPlaca(veiculo.getPlaca().replace("-", ""));
        if (dao.existsByPlaca(veiculo.getPlaca(), connection)) {
            throw new VeiculoExistente("Veículo já cadastrado com essa placa.");
        }
        dao.create(veiculo, connection);
    }
}
