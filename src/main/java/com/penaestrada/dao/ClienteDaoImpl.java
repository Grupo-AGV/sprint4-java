package com.penaestrada.dao;

import com.penaestrada.infra.exceptions.CpfInvalido;
import com.penaestrada.model.Cargo;
import com.penaestrada.model.Cliente;
import com.penaestrada.model.Veiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class ClienteDaoImpl implements ClienteDao {

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

    @Override
    public Cliente findByLogin(String login, Connection connection) throws CpfInvalido, SQLException {
        String sql = "SELECT c.id_usuario, c.nm_usuario, u.ds_email, c.nr_cpf, c.dt_nascimento FROM t_pe_cliente c INNER JOIN t_pe_usuario u ON c.id_usuario = u.id_usuario AND u.ds_email = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("nm_usuario"),
                        rs.getString("nr_cpf"),
                        rs.getDate("dt_nascimento").toString(),
                        rs.getString("ds_email"),
                        null, Cargo.CLIENTE
                );
                cliente.setId(rs.getLong("id_usuario"));
                return cliente;
            }
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException("Erro ao buscar cliente por login");
        } catch (CpfInvalido e) {
            throw new CpfInvalido("CPF inválido");
        }
    }
}
