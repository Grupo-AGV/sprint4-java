package com.penaestrada.dao;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.infra.exceptions.ExclusaoTelefoneUnico;
import com.penaestrada.infra.exceptions.TelefoneNotFound;
import com.penaestrada.model.Telefone;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class TelefoneDaoImpl implements TelefoneDao {

    @Override
    public void create(Telefone telefone, Connection connection) throws SQLException {
        String sql = "INSERT INTO t_pe_telefone (id_usuario, nr_ddi, nr_ddd, nr_telefone) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, telefone.getUsuario().getId());
        preparedStatement.setInt(2, telefone.getDdi());
        preparedStatement.setInt(3, telefone.getDdd());
        preparedStatement.setInt(4, telefone.getNumero());
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Telefone> findAll(Usuario usuario) throws SQLException {
        List<Telefone> resultado = new ArrayList<>();
        String sql = "SELECT * FROM t_pe_telefone WHERE id_usuario = ?";
        try (Connection connection = DatabaseConnectionFactory.create()) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, usuario.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Telefone veiculo = new Telefone(
                        null, rs.getInt("nr_ddi"), rs.getInt("nr_ddd"), rs.getInt("nr_telefone")
                );
                veiculo.setId(rs.getLong("id_telefone"));
                resultado.add(veiculo);
            }
        } catch (SQLException e) {
            throw e;
        }
        return resultado;
    }

    @Override
    public void update(Telefone telefone) throws SQLException, TelefoneNotFound {
        String sql = "UPDATE t_pe_telefone SET nr_ddi = ?, nr_ddd = ?, nr_telefone = ? WHERE id_telefone = ? AND id_usuario = ?";
        try (Connection connection = DatabaseConnectionFactory.create()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, telefone.getDdi());
            preparedStatement.setInt(2, telefone.getDdd());
            preparedStatement.setInt(3, telefone.getNumero());
            preparedStatement.setLong(4, telefone.getId());
            preparedStatement.setLong(5, telefone.getUsuario().getId());
            int linhasAlteradas = preparedStatement.executeUpdate();

            if (linhasAlteradas == 0)
                throw new TelefoneNotFound("Telefone não encontrado.");
        }
    }

    @Override
    public void deleteByIdEUsuarioId(Long idCliente, Long idTelefone) throws SQLException, TelefoneNotFound, ExclusaoTelefoneUnico {
        String countSql = "SELECT COUNT(*) FROM t_pe_telefone WHERE id_usuario = ?";
        String deleteSql = "DELETE FROM t_pe_telefone WHERE id_telefone = ? AND id_usuario = ?";

        try (Connection connection = DatabaseConnectionFactory.create()) {
            try (PreparedStatement countStmt = connection.prepareStatement(countSql)) {
                countStmt.setLong(1, idCliente);
                ResultSet rs = countStmt.executeQuery();
                if (rs.next() && rs.getInt(1) <= 1) {
                    throw new ExclusaoTelefoneUnico();
                }
            }

            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
                deleteStmt.setLong(1, idTelefone);
                deleteStmt.setLong(2, idCliente);
                int rows = deleteStmt.executeUpdate();

                if (rows == 0) {
                    throw new TelefoneNotFound("Telefone não encontrado.");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao excluir veículo", e);
        }
    }
}
