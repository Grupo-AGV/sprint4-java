package com.penaestrada.dao;

import com.penaestrada.infra.exceptions.OrcamentoNotFound;
import com.penaestrada.model.*;

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
        Orcamento orcamento = null;
        String sql = """
                   SELECT orc.*, ve.ds_marca, ve.ds_modelo, ve.nr_ano_lancamento, ve.ds_placa, ser.ds_servico, ser.vl_mao_obra, ser.dt_criacao AS "dt_criacao_servico"\s
                   FROM t_pe_orcamento orc\s
                   INNER JOIN t_pe_veiculo ve ON ve.id_veiculo = orc.id_veiculo AND ve.id_usuario = ?\s
                   INNER JOIN t_pe_oficina ofi ON ofi.id_oficina = orc.id_oficina\s
                   LEFT JOIN t_pe_servico ser ON ser.id_orcamento = orc.id_orcamento\s 
                   WHERE orc.id_orcamento = ?
                """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, idDonoVeiculo);
            pstmt.setLong(2, idOrcamento);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Veiculo veiculo = new Veiculo(null,
                        rs.getString("ds_marca"),
                        rs.getString("ds_modelo"),
                        rs.getString("ds_placa"),
                        rs.getInt("nr_ano_lancamento")
                );
                veiculo.setId(rs.getLong("id_veiculo"));

                LocalDateTime dataFinalizacao = null;
                if (rs.getTimestamp("dt_finalizacao") != null) {
                    dataFinalizacao = rs.getTimestamp("dt_finalizacao").toLocalDateTime();
                }

                orcamento = new Orcamento(veiculo,
                        rs.getString("ds_diagnostico_inicial"),
                        rs.getTimestamp("dt_agendamento").toLocalDateTime(),
                        rs.getTimestamp("dt_criacao").toLocalDateTime(),
                        dataFinalizacao,
                        rs.getDouble("vl_valor_final")
                );
                orcamento.setId(rs.getLong("id_orcamento"));
                Oficina oficina = new Oficina(null, null, Cargo.OFICINA);
                oficina.setId(rs.getLong("id_oficina"));
                orcamento.setOficina(oficina);
            } else {
                throw new OrcamentoNotFound("Orçamento não encontrado.");
            }
            do {
                if (rs.getString("ds_servico") == null) {
                    break;
                }
                Servico servico = new Servico(
                        rs.getString("ds_servico"),
                        rs.getDouble("vl_mao_obra"),
                        rs.getTimestamp("dt_criacao_servico").toLocalDateTime()
                );
                orcamento.getServicos().add(servico);
            } while (rs.next());

            return orcamento;
        }
    }
}
