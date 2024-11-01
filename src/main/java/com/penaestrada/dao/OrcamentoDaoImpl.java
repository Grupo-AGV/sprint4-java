package com.penaestrada.dao;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.infra.exceptions.FinalizarOrcamentoSemServico;
import com.penaestrada.infra.exceptions.OrcamentoJaFinalizado;
import com.penaestrada.infra.exceptions.OrcamentoNotFound;
import com.penaestrada.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public List<Orcamento> findByUsuario(Usuario usuario, Connection connection) throws SQLException {
        List<Orcamento> retorno = new ArrayList<>();
        String sql = """
                SELECT orc.*, usu.id_usuario, usu.ds_email, ve.ds_marca, ve.ds_modelo, 
                        ve.nr_ano_lancamento, ve.ds_placa
                FROM t_pe_orcamento orc
                INNER JOIN t_pe_veiculo ve ON ve.id_veiculo = orc.id_veiculo 
                INNER JOIN t_pe_usuario usu ON usu.id_usuario = ve.id_usuario
                INNER JOIN t_pe_oficina ofi ON ofi.id_oficina = orc.id_oficina
                WHERE (usu.id_usuario = ? AND ? = 'CLIENTE')
                   OR (ofi.id_oficina = ? AND ? = 'OFICINA')
                ORDER BY orc.dt_criacao DESC 
                """;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            Long id = usuario.getId();
            String cargo = usuario.getCargo().toString();
            pstmt.setLong(1, id);
            pstmt.setString(2, cargo);
            pstmt.setLong(3, id);
            pstmt.setString(4, cargo);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente(rs.getString("ds_email"), null, Cargo.CLIENTE);
                cliente.setId(rs.getLong("id_usuario"));
                Veiculo veiculo = new Veiculo(cliente,
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

                Orcamento orcamento = new Orcamento(veiculo,
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
                retorno.add(orcamento);
            }
        }
        return retorno;
    }

    @Override
    public Orcamento findByIdEUsuario(Usuario usuario, Long idOrcamento, Connection connection) throws SQLException, OrcamentoNotFound {
        Orcamento orcamento = null;
        String sql = """
                SELECT orc.*, usu.id_usuario, usu.ds_email, ve.ds_marca, ve.ds_modelo, 
                       ve.nr_ano_lancamento, ve.ds_placa, ser.id_servico, ser.ds_servico, 
                       ser.vl_mao_obra, ser.dt_criacao AS "dt_criacao_servico"
                FROM t_pe_orcamento orc
                INNER JOIN t_pe_veiculo ve ON ve.id_veiculo = orc.id_veiculo 
                INNER JOIN t_pe_usuario usu ON usu.id_usuario = ve.id_usuario
                INNER JOIN t_pe_oficina ofi ON ofi.id_oficina = orc.id_oficina
                LEFT JOIN t_pe_servico ser ON ser.id_orcamento = orc.id_orcamento
                WHERE orc.id_orcamento = ? 
                AND ( 
                     (usu.id_usuario = ? AND ? = 'CLIENTE') 
                     OR (ofi.id_oficina = ? AND ? = 'OFICINA')
                )
                """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, idOrcamento);
            pstmt.setLong(2, usuario.getId());
            pstmt.setString(3, usuario.getCargo().toString());
            pstmt.setLong(4, usuario.getId());
            pstmt.setString(5, usuario.getCargo().toString());

            orcamento = formatarOrcamento(pstmt);
            return orcamento;
        }
    }

    @Override
    public void finalizarOrcamento(Usuario usuario, Long id) throws OrcamentoNotFound, FinalizarOrcamentoSemServico, SQLException {
        String sqlCount = "SELECT COUNT(*), SUM(vl_mao_obra) FROM T_PE_SERVICO WHERE ID_ORCAMENTO = ?";
        String sqlUpdate = "UPDATE T_PE_ORCAMENTO SET DT_FINALIZACAO = ?, VL_VALOR_FINAL = ? WHERE ID_ORCAMENTO = ?";

        try (Connection connection = DatabaseConnectionFactory.create();
             PreparedStatement pstmtSelect = connection.prepareStatement(sqlCount)) {
            pstmtSelect.setLong(1, id);
            try (ResultSet rs = pstmtSelect.executeQuery()) {
                if (rs.next()) {
                    if (rs.getInt(1) == 0)
                        throw new FinalizarOrcamentoSemServico("Orçamento não possui serviços.");


                    double valorTotal = rs.getDouble(2);

                    try (PreparedStatement pstmtUpdate = connection.prepareStatement(sqlUpdate)) {
                        pstmtUpdate.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                        pstmtUpdate.setDouble(2, valorTotal);
                        pstmtUpdate.setLong(3, id);

                        int rowsUpdated = pstmtUpdate.executeUpdate();
                        if (rowsUpdated == 0)
                            throw new OrcamentoNotFound("Orçamento não encontrado.");
                    }
                }
            }
        }
    }

    @Override
    public void verificarOrcamentoFinalizado(Long id) throws SQLException, OrcamentoJaFinalizado, OrcamentoNotFound {
        String sqlCheckFinalizado = "SELECT DT_FINALIZACAO FROM T_PE_ORCAMENTO WHERE ID_ORCAMENTO = ?";
        try (Connection connection = DatabaseConnectionFactory.create();
             PreparedStatement pstmtCheck = connection.prepareStatement(sqlCheckFinalizado)) {
            pstmtCheck.setLong(1, id);
            try (ResultSet rsCheck = pstmtCheck.executeQuery()) {
                if (rsCheck.next() && rsCheck.getTimestamp("DT_FINALIZACAO") != null)
                    throw new OrcamentoJaFinalizado("O orçamento já foi finalizado.");
                else
                    throw new OrcamentoNotFound("Orçamento não encontrado.");
            }
        }
    }

    @Override
    public void verificarSeOrcamentoDoUsuario(Usuario usuario, Long idOrcamento) throws SQLException, OrcamentoNotFound {
        String sql = """
                SELECT o.id_orcamento AS "exists"
                FROM t_pe_orcamento o
                LEFT JOIN t_pe_veiculo v ON o.id_veiculo = v.id_veiculo
                WHERE (v.id_usuario = ? OR o.id_oficina = ?)
                  AND o.id_orcamento = ?
                """;
        try (Connection connection = DatabaseConnectionFactory.create()) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, usuario.getId());
            pstmt.setLong(2, usuario.getId());
            pstmt.setLong(3, idOrcamento);

            ResultSet rs = pstmt.executeQuery();

            if (!rs.next())
                throw new OrcamentoNotFound("Orçamento não encontrado");
        }

    }

    private static Orcamento formatarOrcamento(PreparedStatement pstmt) throws SQLException, OrcamentoNotFound {
        Orcamento orcamento;
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            Cliente cliente = new Cliente(rs.getString("ds_email"), null, Cargo.CLIENTE);
            cliente.setId(rs.getLong("id_usuario"));
            Veiculo veiculo = new Veiculo(cliente,
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
            servico.setId(rs.getLong("id_servico"));
            orcamento.getServicos().add(servico);
        } while (rs.next());
        return orcamento;
    }
}
