package com.penaestrada.dao;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.infra.exceptions.LoginNotFound;
import com.penaestrada.model.Cargo;
import com.penaestrada.model.Cliente;
import com.penaestrada.model.Oficina;
import com.penaestrada.model.Usuario;
import oracle.jdbc.OracleType;

import java.sql.*;

public class UsuarioDaoImpl implements UsuarioDao {

    @Override
    public void create(Usuario usuario, Connection connection) throws SQLException {
        final String sql = "BEGIN INSERT INTO T_PE_USUARIO(ds_email, ds_senha, ds_cargo) VALUES (?, ?, ?) RETURNING id_usuario INTO ?; END;";

        CallableStatement call = connection.prepareCall(sql);
        call.setString(1, usuario.getEmail());
        call.setString(2, usuario.getSenha());
        call.setString(3, usuario.getCargo().getDescricao());
        call.registerOutParameter(4, OracleType.NUMBER);

        int linhasAlteradas = call.executeUpdate();

        if (linhasAlteradas == 0)
            throw new SQLException("Erro ao inserir usuario."); // TODO trocar exception dps

        long id = call.getLong(4);

        if (id == 0)
            throw new SQLException("Erro ao inserir usuario."); // TODO trocar exception dps
        usuario.setId(id);
    }

    @Override
    public Usuario findByEmail(String email, Connection connection) throws SQLException, LoginNotFound {
        String sql = "SELECT id_usuario, ds_senha, ds_cargo FROM T_PE_USUARIO WHERE ds_email = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String cargo = rs.getString("ds_cargo");
                if (Cargo.CLIENTE.getDescricao().equals(cargo)) {
                    return new Cliente(null, rs.getString("ds_senha"), Cargo.valueOf(cargo));
                } else if (Cargo.OFICINA.getDescricao().equals(cargo)) {
                    return new Oficina(null, rs.getString("ds_senha"), Cargo.valueOf(cargo));
                }
            }
            throw new LoginNotFound("Login não encontrado");
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } catch (LoginNotFound e) {
            throw new LoginNotFound(e.getMessage());
        }
    }
}
