package com.penaestrada.service;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dao.OrcamentoDao;
import com.penaestrada.dao.OrcamentoDaoFactory;
import com.penaestrada.dto.*;
import com.penaestrada.infra.DefaultDateFormatter;
import com.penaestrada.infra.exceptions.ClienteNotFound;
import com.penaestrada.infra.exceptions.CpfInvalido;
import com.penaestrada.infra.exceptions.OrcamentoNotFound;
import com.penaestrada.infra.exceptions.VeiculoNotFound;
import com.penaestrada.infra.security.OficinaNotFound;
import com.penaestrada.model.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

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
    public DetalhesOrcamentoDto findByIdEUsuario(Usuario usuario, Long id) throws SQLException, CpfInvalido, OrcamentoNotFound, ClienteNotFound, OficinaNotFound {
        try (Connection connection = DatabaseConnectionFactory.create()) {
            Orcamento orcamento = dao.findByIdEUsuario(usuario, id, connection);
            DetalhesClienteOrcamentoDto cliente = clienteService.detalhesOrcamentoCliente(orcamento.getUsuario(), connection);
            DetalhesOficinaDto oficina = oficinaService.detalhesOficinaPorId(orcamento.getOficina().getId(), connection);
            Veiculo veiculo = orcamento.getVeiculo();
            DetalhesVeiculoDto detalhesVeiculo = getDetalhesVeiculoDto(veiculo);
            List<DetalhesServicoDto> servicos = getDetalhesServicoDtos(orcamento);
            String dataFinalizacao = null;
            if (orcamento.getDataFinalizacao() != null) {
                dataFinalizacao = orcamento.getDataFinalizacao().toString();
            }
            Double valorFinal = null;
            if (orcamento.getValorFinal() != 0.0) {
                valorFinal = orcamento.getValorFinal();
            }
            return new DetalhesOrcamentoDto(
                    orcamento.getId(), cliente, detalhesVeiculo, oficina, orcamento.getDiagnosticoInicial(),
                    orcamento.getDataAgendamento().toString(), orcamento.getDataCriacao().toString(), valorFinal,
                    dataFinalizacao, servicos
            );
        }
    }

    private static DetalhesVeiculoDto getDetalhesVeiculoDto(Veiculo veiculo) {
        return new DetalhesVeiculoDto(
                veiculo.getId(), veiculo.getMarca(), veiculo.getModelo(),
                veiculo.getAnoLancamento().toString(), veiculo.getPlaca()
        );
    }

    private static List<DetalhesServicoDto> getDetalhesServicoDtos(Orcamento orcamento) {
        return orcamento.getServicos().stream().map(s -> new DetalhesServicoDto(
                s.getId(), s.getDescricao(), s.getValorMaoDeObra(), s.getDataCriacao().toString()
        )).toList();
    }
}
