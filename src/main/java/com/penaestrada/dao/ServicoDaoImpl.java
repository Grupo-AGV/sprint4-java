package com.penaestrada.dao;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.model.Servico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

class ServicoDaoImpl implements ServicoDao {

    @Override
    public void criarServico(Long idOrcamento, Servico servico) throws SQLException {
        String sql = """
                INSERT INTO t_pe_servico (id_orcamento, ds_servico, vl_mao_obra, dt_criacao) VALUES(?, ?, ?, ?)
                """;
        try (Connection connection = DatabaseConnectionFactory.create();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, idOrcamento);
            pstmt.setString(2, servico.getDescricao());
            pstmt.setDouble(3, servico.getValorMaoDeObra());
            pstmt.setTimestamp(4, Timestamp.valueOf(servico.getDataCriacao()));
            pstmt.executeUpdate();
        }
    }
}
