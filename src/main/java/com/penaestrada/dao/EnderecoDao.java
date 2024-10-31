package com.penaestrada.dao;

import com.penaestrada.model.Endereco;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface EnderecoDao {

    void create(Endereco endereco, Connection connection) throws SQLException;

    List<Endereco> findEnderecoByUsuario(Usuario usuario) throws SQLException;
}
