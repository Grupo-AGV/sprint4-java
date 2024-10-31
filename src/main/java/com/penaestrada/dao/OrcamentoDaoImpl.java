package com.penaestrada.dao;

import com.penaestrada.model.Orcamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

class OrcamentoDaoImpl implements OrcamentoDao {

    @Override
    public void agendarOrcamento(Orcamento orcamento, Connection connection) throws SQLException {
        String sql = "INSERT INTO t_pe_orcamento (id_oficina, id_veiculo, ds_diagnostico_inicial, dt_agendamento, dt_criacao) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setLong(1, orcamento.getOficina().getId());
        pstmt.setLong(2, orcamento.getVeiculo().getId());
        pstmt.setString(3, orcamento.getDiagnosticoInicial());
        Timestamp agendamentoTimestamp = Timestamp.valueOf(orcamento.getDataAgendamento());
        pstmt.setTimestamp(4, agendamentoTimestamp);
        Timestamp criacaoTimestamp = Timestamp.valueOf(orcamento.getDataCriacao());
        pstmt.setTimestamp(5, criacaoTimestamp);
        pstmt.executeUpdate();
    }
}
