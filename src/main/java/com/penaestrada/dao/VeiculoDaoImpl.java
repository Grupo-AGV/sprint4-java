package com.penaestrada.dao;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.model.Veiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VeiculoDaoImpl implements VeiculoDao {


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
            System.out.println(e.getMessage());
            throw new SQLException("Erro ao criar veículo");
        }
    }

    @Override
    public Boolean existsByPlaca(String placa, Connection connection) throws SQLException {
        String sql = "SELECT * FROM T_PE_VEICULO WHERE ds_placa = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, placa);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Retorna true se existe um veículo com a placa fornecida
        } catch (SQLException e) {
            throw new SQLException("Erro ao buscar veículo por placa", e);
        }
    }

    @Override
    public void deleteById(Long id, Connection connection) throws SQLException {

    }
}
