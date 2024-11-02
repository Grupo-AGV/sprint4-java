package com.penaestrada.service;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dao.ClienteDao;
import com.penaestrada.dao.ClienteDaoFactory;
import com.penaestrada.dto.*;
import com.penaestrada.infra.exceptions.*;
import com.penaestrada.model.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class ClienteServiceImpl implements ClienteService {

    private final ClienteDao dao = ClienteDaoFactory.create();

    private final UsuarioService usuarioService = UsuarioServiceFactory.create();

    private final VeiculoService veiculoService = VeiculoServiceFactory.create();

    private final TelefoneService telefoneService = TelefoneServiceFactory.create();

    private final OficinaService oficinaService = OficinaServiceFactory.create();

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

    @Override
    public IniciarChatBot iniciarChatBot(Cliente cliente) throws ClienteNotFound, SQLException, CpfInvalido {
        try (Connection connection = DatabaseConnectionFactory.create()) {
            Cliente c = dao.findByLogin(cliente.getEmail(), connection);
            List<Veiculo> veiculos = veiculoService.findVeiculosByClienteId(c.getId(), connection);
            List<DetalhesOficinaDto> oficinas = oficinaService.listarOficinas();
            List<ResponseSimples> veiculosDto = veiculos.stream().map(v -> new ResponseSimples(v.getId(), v.getMarca() + " " + v.getModelo() + " " + v.getAnoLancamento())).toList();
            List<ResponseSimples> oficinasDto = oficinas.stream().map(o -> new ResponseSimples(o.id(), o.name())).toList();
            return new IniciarChatBot(
                    c.getNome(), veiculosDto, oficinasDto
            );
        }
    }
}
