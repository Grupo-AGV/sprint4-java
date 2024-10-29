package com.penaestrada.dao;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDaoImpl implements ClienteDao {

    @Override
    public void create(Cliente cliente, Connection connection) throws SQLException {
        final String sql = "INSERT INTO T_PE_CLIENTE(id_usuario, nm_usuario, nr_cpf, dt_nascimento) VALUES (?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'))";

        PreparedStatement pstmt = connection.prepareCall(sql);
        pstmt.setLong(1, cliente.getId());
        pstmt.setString(2, cliente.getNome());
        pstmt.setString(3, cliente.getCpf());
        pstmt.setString(4, cliente.getDataNascimento().toString());
        pstmt.executeUpdate();
    }

    @Override
    public Boolean existsByCpf(String cpf, Connection connection) throws SQLException {
        String sql = "SELECT * FROM T_PE_CLIENTE WHERE nr_cpf = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, cpf);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new SQLException("Erro ao buscar cliente por cpf");
        }
    }
}
