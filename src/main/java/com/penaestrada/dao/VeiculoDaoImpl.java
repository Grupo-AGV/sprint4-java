package com.penaestrada.dao;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.infra.exceptions.ExclusaoVeiculoUnico;
import com.penaestrada.infra.exceptions.VeiculoNotFound;
import com.penaestrada.model.Veiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class VeiculoDaoImpl implements VeiculoDao {


    @Override
    public void create(Veiculo veiculo, Connection connection) throws SQLException {
        String sql = "INSERT INTO T_PE_VEICULO (id_usuario, ds_marca, ds_modelo, nr_ano_lancamento, ds_placa) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, veiculo.getCliente().getId());
            pstmt.setString(2, veiculo.getMarca());
            pstmt.setString(3, veiculo.getModelo());
            pstmt.setInt(4, veiculo.getAnoLancamento());
            pstmt.setString(5, veiculo.getPlaca());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Erro ao criar veículo");
        }
    }

    @Override
    public Boolean existsByPlaca(String placa, Connection connection) throws SQLException {
        String sql = "SELECT * FROM T_PE_VEICULO WHERE ds_placa = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, placa);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Retorna true se existe um veículo com a licensePlate fornecida
        } catch (SQLException e) {
            throw new SQLException("Erro ao buscar veículo por licensePlate", e);
        }
    }

    @Override
    public List<Veiculo> findVeiculosByClienteId(Long id) throws SQLException {
        List<Veiculo> resultado = new ArrayList<>();
        String sql = "SELECT id_veiculo, ds_marca, ds_modelo, nr_ano_lancamento, ds_placa FROM t_pe_veiculo WHERE id_usuario = ?  ORDER BY id_veiculo DESC";
        try (Connection connection = DatabaseConnectionFactory.create()) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Veiculo veiculo = new Veiculo(
                        null,
                        rs.getString("ds_marca"),
                        rs.getString("ds_modelo"),
                        rs.getString("ds_placa"),
                        rs.getInt("nr_ano_lancamento")
                );
                veiculo.setId(rs.getLong("id_veiculo"));
                resultado.add(veiculo);
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao buscar veículos por cliente");
        }
        return resultado;
    }

    @Override
    public void deleteByIdEClienteId(Long idCliente, Long idVeiculo) throws SQLException, VeiculoNotFound, ExclusaoVeiculoUnico {
        String countSql = "SELECT COUNT(*) FROM t_pe_veiculo WHERE id_usuario = ?";
        String deleteSql = "DELETE FROM t_pe_veiculo WHERE id_veiculo = ? AND id_usuario = ?";

        try (Connection connection = DatabaseConnectionFactory.create()) {
            try (PreparedStatement countStmt = connection.prepareStatement(countSql)) {
                countStmt.setLong(1, idCliente);
                ResultSet rs = countStmt.executeQuery();
                if (rs.next() && rs.getInt(1) <= 1) {
                    throw new ExclusaoVeiculoUnico();
                }
            }

            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
                deleteStmt.setLong(1, idVeiculo);
                deleteStmt.setLong(2, idCliente);
                int rows = deleteStmt.executeUpdate();

                if (rows == 0) {
                    throw new VeiculoNotFound("Veículo não encontrado.");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao excluir veículo", e);
        }
    }

    @Override
    public Veiculo findByIdEClienteId(Long idCliente, Long idVeiculo, Connection connection) throws SQLException, VeiculoNotFound {
        String sql = "SELECT id_veiculo, ds_marca, ds_modelo, nr_ano_lancamento, ds_placa FROM t_pe_veiculo WHERE id_usuario = ? AND id_veiculo = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setLong(1, idCliente);
        pstmt.setLong(2, idVeiculo);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            Veiculo veiculo = new Veiculo(
                    null,
                    rs.getString("ds_marca"),
                    rs.getString("ds_modelo"),
                    rs.getString("ds_placa"),
                    rs.getInt("nr_ano_lancamento")
            );
            veiculo.setId(rs.getLong("id_veiculo"));
            return veiculo;
        }
        throw new VeiculoNotFound("Veículo não encontrado");
    }
}
