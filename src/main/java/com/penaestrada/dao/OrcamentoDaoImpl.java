package com.penaestrada.dao;

import com.penaestrada.infra.exceptions.OrcamentoNotFound;
import com.penaestrada.model.Orcamento;
import com.penaestrada.model.Veiculo;

import java.sql.*;
import java.time.LocalDateTime;

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

    @Override
    public Orcamento findByIdEDonoVeiculo(Long idDonoVeiculo, Long idOrcamento, Connection connection) throws SQLException, OrcamentoNotFound {
        String sql = """
                   SELECT orc.*, ve.ds_marca, ve.ds_modelo, ve.nr_ano_lancamento, ve.ds_placa\s
                   FROM t_pe_orcamento orc\s
                   INNER JOIN t_pe_veiculo ve ON ve.id_veiculo = orc.id_veiculo AND ve.id_usuario = ?\s
                   INNER JOIN t_pe_oficina ofi ON ofi.id_oficina = orc.id_oficina\s
                   INNER JOIN t_pe_diagnostico 
                   WHERE orc.id_orcamento = ?
                """;
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setLong(1, idDonoVeiculo);
        pstmt.setLong(2, idOrcamento);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            Veiculo veiculo = new Veiculo(null,
                    rs.getString("ds_marca"), rs.getString("ds_modelo"),
                    rs.getString("ds_placa"), rs.getInt("nr_ano_lancamento")
            );
            Orcamento orcamento = new Orcamento(veiculo, rs.getString("ds_diagnostico_inicial"),
                    rs.getTimestamp("dt_agendamento").toLocalDateTime(),
                    rs.getTimestamp("dt_criacao").toLocalDateTime(),
                    rs.getTimestamp("dt_finalizacao").toLocalDateTime(),
                    rs.getDouble("vl_valor_final"));

        }
        throw new OrcamentoNotFound("Orçamento não encontrado");
    }
}
