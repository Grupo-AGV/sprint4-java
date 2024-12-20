package com.penaestrada.service;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dao.OrcamentoDao;
import com.penaestrada.dao.OrcamentoDaoFactory;
import com.penaestrada.dto.*;
import com.penaestrada.infra.DefaultDateFormatter;
import com.penaestrada.infra.exceptions.*;
import com.penaestrada.infra.exceptions.OficinaNotFound;
import com.penaestrada.model.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public List<DetalhesOrcamentoDto> findByUsuario(Usuario usuario) throws SQLException, ClienteNotFound, CpfInvalido, OficinaNotFound {
        List<DetalhesOrcamentoDto> retorno = new ArrayList<>();
        try (Connection connection = DatabaseConnectionFactory.create()) {
            List<Orcamento> orcamentos = dao.findByUsuario(usuario, connection);
            for (Orcamento orcamento : orcamentos) {
                DetalhesOrcamentoDto orcamentoDto = getDetalhesOrcamentoDto(orcamento, connection);
                retorno.add(orcamentoDto);
            }
        }
        return retorno;
    }

    @Override
    public DetalhesOrcamentoDto findByIdEUsuario(Usuario usuario, Long id) throws SQLException, CpfInvalido, OrcamentoNotFound, ClienteNotFound, OficinaNotFound {
        try (Connection connection = DatabaseConnectionFactory.create()) {
            Orcamento orcamento = dao.findByIdEUsuario(usuario, id, connection);
            return getDetalhesOrcamentoDto(orcamento, connection);
        }
    }

    private DetalhesOrcamentoDto getDetalhesOrcamentoDto(Orcamento orcamento, Connection connection) throws SQLException, CpfInvalido, ClienteNotFound, OficinaNotFound {
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

    @Override
    public void finalizarOrcamento(Usuario usuario, Long id) throws OrcamentoNotFound, SQLException, FinalizarOrcamentoSemServico, OrcamentoJaFinalizado {
        verificarSeOrcamentoFinalizado(id);
        dao.finalizarOrcamento(usuario, id);
    }

    @Override
    public void verificarSeOrcamentoFinalizado(Long id) throws OrcamentoJaFinalizado, SQLException {
        dao.verificarOrcamentoFinalizado(id);
    }

    @Override
    public void verificarSeOrcamentoDoUsuario(Usuario usuario, Long id) throws OrcamentoNotFound, SQLException {
        dao.verificarSeOrcamentoDoUsuario(usuario, id);
    }

    private DetalhesVeiculoDto getDetalhesVeiculoDto(Veiculo veiculo) {
        return new DetalhesVeiculoDto(
                veiculo.getId(), veiculo.getMarca(), veiculo.getModelo(),
                veiculo.getAnoLancamento().toString(), veiculo.getPlaca()
        );
    }

    private List<DetalhesServicoDto> getDetalhesServicoDtos(Orcamento orcamento) {
        return orcamento.getServicos().stream().map(s -> new DetalhesServicoDto(
                s.getId(), s.getDescricao(), s.getValorMaoDeObra(), s.getDataCriacao().toString()
        )).toList();
    }
}
