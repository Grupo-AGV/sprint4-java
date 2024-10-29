package com.penaestrada.dao;

import com.penaestrada.infra.exceptions.LoginNotFound;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;

public interface UsuarioDao {

    void create(Usuario usuario, Connection connection) throws SQLException;

    Usuario findByEmail(String email, Connection connection) throws SQLException, LoginNotFound;
}
