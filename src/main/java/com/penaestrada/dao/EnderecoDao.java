package com.penaestrada.dao;

import com.penaestrada.model.Endereco;

import java.sql.Connection;
import java.sql.SQLException;

public interface EnderecoDao {

    void create(Endereco endereco, Connection connection) throws SQLException;
}
