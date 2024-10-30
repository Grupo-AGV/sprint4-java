package com.penaestrada.service;

import com.penaestrada.infra.exceptions.EmailExistente;
import com.penaestrada.infra.exceptions.LoginInvalido;
import com.penaestrada.infra.exceptions.LoginNotFound;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;

public interface UsuarioService {

    void create(Usuario usuario, Connection connection) throws SQLException, EmailExistente;

    Usuario logarUsuario(String email, String senha) throws SQLException, LoginInvalido, LoginNotFound;

    Usuario findByLogin(String email) throws SQLException, LoginNotFound;

}
