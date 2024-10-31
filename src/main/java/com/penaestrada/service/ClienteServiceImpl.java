package com.penaestrada.service;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dao.ClienteDao;
import com.penaestrada.dao.ClienteDaoFactory;
import com.penaestrada.dto.ClienteDashboardDto;
import com.penaestrada.dto.DetalhesClienteOrcamentoDto;
import com.penaestrada.dto.DetalhesTelefoneDto;
import com.penaestrada.dto.DetalhesVeiculoDto;
import com.penaestrada.infra.exceptions.*;
import com.penaestrada.model.Cliente;
import com.penaestrada.model.Telefone;
import com.penaestrada.model.Usuario;
import com.penaestrada.model.Veiculo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class ClienteServiceImpl implements ClienteService {

    private final ClienteDao dao = ClienteDaoFactory.create();

    private final UsuarioService usuarioService = UsuarioServiceFactory.create();

    private final VeiculoService veiculoService = VeiculoServiceFactory.create();

    private final TelefoneService telefoneService = TelefoneServiceFactory.create();

    @Override
    public void create(Cliente cliente) throws SQLException, CpfExistente, EmailExistente, VeiculoExistente {
        Connection connection = DatabaseConnectionFactory.create();
        try {
            usuarioService.create(cliente, connection);

            if (dao.existsByCpf(cliente.getCpf(), connection)) {
                throw new CpfExistente("Cliente já cadastrado com esse CPF.");
            }
            dao.create(cliente, connection);

            veiculoService.create(cliente.getVeiculos().get(0), connection);
            connection.commit();
        } catch (EmailExistente | VeiculoExistente | SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    @Override
    public ClienteDashboardDto dashboard(String login) throws ClienteNotFound, SQLException, CpfInvalido {
        try (Connection connection = DatabaseConnectionFactory.create()) {
            Cliente cliente = dao.findByLogin(login, connection);
            if (cliente == null)
                throw new ClienteNotFound();
            List<Veiculo> veiculos = veiculoService.findVeiculosByClienteId(cliente.getId(), connection);
            List<Telefone> contatos = telefoneService.buscarTelefonesPorUsuario(cliente, connection);
            return new ClienteDashboardDto(
                    cliente.getId(), cliente.getNome(), cliente.getEmail(),
                    cliente.getCpf(), cliente.getDataNascimento().toString(),
                    veiculos.stream().map(v -> new DetalhesVeiculoDto(
                            v.getId(), v.getMarca(), v.getModelo(),
                            v.getAnoLancamento().toString(), v.getPlaca())).toList(),
                    contatos.stream().map(t -> new DetalhesTelefoneDto(t.getId(), t.getNumeroCompleto())).toList()
            );
        }
    }

    @Override
    public DetalhesClienteOrcamentoDto detalhesOrcamentoCliente(Usuario usuario, Connection connection) throws SQLException, CpfInvalido, ClienteNotFound {
        Cliente cliente = dao.findByLogin(usuario.getEmail(), connection);
        List<Telefone> contatos = telefoneService.buscarTelefonesPorUsuario(usuario, connection);
        cliente.setContatos(contatos);
        return new DetalhesClienteOrcamentoDto(
                cliente.getId(), cliente.getNome(), cliente.getEmail(),
                cliente.getContatos().stream().map(t -> new DetalhesTelefoneDto(t.getId(), t.getNumeroCompleto())).toList()
        );
    }
}
