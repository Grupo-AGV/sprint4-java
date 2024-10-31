package com.penaestrada.service;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dao.VeiculoDao;
import com.penaestrada.dao.VeiculoDaoFactory;
import com.penaestrada.infra.exceptions.ExclusaoVeiculoUnico;
import com.penaestrada.infra.exceptions.VeiculoExistente;
import com.penaestrada.infra.exceptions.VeiculoNotFound;
import com.penaestrada.model.Cliente;
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

    @Override
    public void removerVeiculoDoCliente(Cliente cliente, Long id) throws SQLException, VeiculoNotFound, ExclusaoVeiculoUnico {
        dao.deleteByIdEClienteId(cliente.getId(), id);
    }

    @Override
    public Veiculo findByIdEClienteId(Long idCliente, Long id, Connection connection) throws SQLException, VeiculoNotFound {
        return dao.findByIdEClienteId(idCliente, id, connection);
    }

    private void adicionarVeiculo(Veiculo veiculo, Connection connection) throws SQLException, VeiculoExistente {
        veiculo.setPlaca(veiculo.getPlaca().replace("-", ""));
        if (dao.existsByPlaca(veiculo.getPlaca(), connection)) {
            throw new VeiculoExistente("Veículo já cadastrado com essa placa.");
        }
        dao.create(veiculo, connection);
    }
    
}
