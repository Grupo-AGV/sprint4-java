package com.penaestrada.service;

import com.penaestrada.dao.ServicoDao;
import com.penaestrada.dao.ServicoDaoFactory;
import com.penaestrada.dto.CriarServicoDto;
import com.penaestrada.model.Servico;

import java.sql.SQLException;
import java.time.LocalDateTime;

class ServicoServiceImpl implements ServicoService {

    private ServicoDao servicoDao = ServicoDaoFactory.create();

    @Override
    public void adicionarServico(Long idOrcamento, CriarServicoDto dto) throws SQLException {
        Servico servico = new Servico(dto.description(), dto.price(), LocalDateTime.now());
        servicoDao.criarServico(idOrcamento, servico);
    }
}
