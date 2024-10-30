package com.penaestrada.dao;

import com.penaestrada.model.Telefone;
import com.penaestrada.model.Usuario;

import java.sql.*;
import java.util.List;

class TelefoneDaoImpl implements TelefoneDao {

    @Override
    public void create(Telefone telefone, Connection connection) {

    }

    @Override
    public List<Telefone> findAll(Usuario usuario) {
        return List.of();
    }

    @Override
    public void update(Telefone telefone, Connection connection) {

    }

    @Override
    public void deleteById(Long id, Connection connection) {

    }
}
