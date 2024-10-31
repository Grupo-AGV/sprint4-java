package com.penaestrada.service;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dao.OrcamentoDao;
import com.penaestrada.dao.OrcamentoDaoFactory;
import com.penaestrada.dto.CriarOrcamentoDto;
import com.penaestrada.dto.DetalhesOrcamentoDto;
import com.penaestrada.infra.DefaultDateFormatter;
import com.penaestrada.infra.exceptions.CpfInvalido;
import com.penaestrada.infra.exceptions.VeiculoNotFound;
import com.penaestrada.infra.security.OficinaNotFound;
import com.penaestrada.model.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

class OrcamentoServiceImpl implements OrcamentoService {

    private final VeiculoService veiculoService = VeiculoServiceFactory.create();
    private final OficinaService oficinaService = OficinaServiceFactory.create();
    private final ClienteService clienteService = ClienteServiceFactory.create();
    private final OrcamentoDao dao = OrcamentoDaoFactory.create();

    @Override
    public void agendarOrcamento(Cliente cliente, CriarOrcamentoDto dto) throws SQLException, VeiculoNotFound, OficinaNotFound {
        Connection connection = DatabaseConnectionFactory.create();
        try {
            Veiculo veiculo = veiculoService.findByIdEClienteId(cliente.getId(), dto.vehicleId(), connection);
            veiculo.setCliente(cliente);
            Oficina oficina = oficinaService.findById(dto.workshopId(), connection);
            Orcamento orcamento = new Orcamento(oficina, veiculo, dto.description(), LocalDateTime.parse(dto.scheduledAt(), DefaultDateFormatter.formatter));
            dao.agendarOrcamento(orcamento, connection);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        }
    }

    @Override
    public DetalhesOrcamentoDto findByIdEUsuario(Usuario usuario, Long id) throws SQLException, CpfInvalido {
        Connection connection = DatabaseConnectionFactory.create();
        Orcamento orcamento = dao.findByIdEDonoVeiculo(usuario.getId(), id, connection);
        Cliente cliente = clienteService.detalhesOrcamentoCliente(usuario, connection);
    }
}
