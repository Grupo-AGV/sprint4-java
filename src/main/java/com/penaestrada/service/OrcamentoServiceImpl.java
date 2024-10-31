package com.penaestrada.service;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dao.OrcamentoDao;
import com.penaestrada.dao.OrcamentoDaoFactory;
import com.penaestrada.dto.CriarOrcamentoDto;
import com.penaestrada.infra.DefaultDateFormatter;
import com.penaestrada.infra.exceptions.VeiculoNotFound;
import com.penaestrada.infra.security.OficinaNotFound;
import com.penaestrada.model.Cliente;
import com.penaestrada.model.Oficina;
import com.penaestrada.model.Orcamento;
import com.penaestrada.model.Veiculo;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

class OrcamentoServiceImpl implements OrcamentoService {

    private final VeiculoService veiculoService = VeiculoServiceFactory.create();
    private final OficinaService oficinaService = OficinaServiceFactory.create();
    private final OrcamentoDao dao = OrcamentoDaoFactory.create();

    @Override
    public void agendarOrcamento(Cliente cliente, CriarOrcamentoDto dto) throws SQLException, VeiculoNotFound, OficinaNotFound {
        Connection connection = DatabaseConnectionFactory.create();
        try {
            Veiculo veiculo = veiculoService.findByIdEClienteId(cliente.getId(), dto.vehicleId(), connection);
            Oficina oficina = oficinaService.findById(dto.workshopId(), connection);
            Orcamento orcamento = new Orcamento(oficina, veiculo, dto.description(), LocalDateTime.parse(dto.scheduledAt(), DefaultDateFormatter.formatter));
            dao.agendarOrcamento(orcamento, connection);
        } catch (Exception e) {
            connection.rollback();
            throw e;
        }
    }
}
