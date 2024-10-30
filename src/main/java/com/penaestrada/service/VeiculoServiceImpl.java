package com.penaestrada.service;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dao.VeiculoDao;
import com.penaestrada.dao.VeiculoDaoFactory;
import com.penaestrada.infra.exceptions.VeiculoExistente;
import com.penaestrada.model.Veiculo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class VeiculoServiceImpl implements VeiculoService {

    private final VeiculoDao dao = VeiculoDaoFactory.create();

    @Override
    public void create(Veiculo veiculo, Connection connection) throws SQLException, VeiculoExistente {
        adicionarVeiculo(veiculo, connection);
    }

    @Override
    public void adionarVeiculoAoCliente(Veiculo veiculo) throws SQLException, VeiculoExistente {
        try (Connection connection = DatabaseConnectionFactory.create()) {
            adicionarVeiculo(veiculo, connection);
        }
    }

    @Override
    public List<Veiculo> findVeiculosByClienteId(Long idCliente) throws SQLException {
        return dao.findVeiculosByClienteId(idCliente);
    }

    private void adicionarVeiculo(Veiculo veiculo, Connection connection) throws SQLException, VeiculoExistente {
        veiculo.setPlaca(veiculo.getPlaca().replace("-", ""));
        if (dao.existsByPlaca(veiculo.getPlaca(), connection)) {
            throw new VeiculoExistente("Veículo já cadastrado com essa placa.");
        }
        dao.create(veiculo, connection);
    }
    
}
