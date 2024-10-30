package com.penaestrada.service;

import com.penaestrada.dao.ClienteDao;
import com.penaestrada.dao.ClienteDaoImpl;
import com.penaestrada.dto.ClienteDashboardDto;
import com.penaestrada.dto.DetalhesVeiculo;
import com.penaestrada.infra.exceptions.ClienteNotFound;
import com.penaestrada.infra.exceptions.CpfExistente;
import com.penaestrada.infra.exceptions.CpfInvalido;
import com.penaestrada.model.Cliente;
import com.penaestrada.model.Veiculo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class ClienteServiceImpl implements ClienteService {

    private final ClienteDao dao = new ClienteDaoImpl();

    @Override
    public void create(Cliente cliente, Connection connection) throws SQLException, CpfExistente {
        if (dao.existsByCpf(cliente.getCpf(), connection)) {
            throw new CpfExistente("Cliente já cadastrado com esse CPF.");
        }
        dao.create(cliente, connection);
    }

    @Override
    public ClienteDashboardDto dashboard(String login, Connection connection) throws ClienteNotFound, SQLException, CpfInvalido {
        Cliente cliente = dao.findByLogin(login, connection);
        if (cliente == null)
            throw new ClienteNotFound();
        List<Veiculo> veiculos = dao.findVeiculosByCliente(cliente, connection);
        return new ClienteDashboardDto(
                cliente.getId(), cliente.getNome(), cliente.getEmail(),
                cliente.getCpf(), cliente.getDataNascimento().toString(),
                veiculos.stream().map(v -> new DetalhesVeiculo(
                        v.getId(), v.getMarca(), v.getModelo(),
                        v.getAnoLancamento().toString(), v.getPlaca())).toList()
        );
    }
}
