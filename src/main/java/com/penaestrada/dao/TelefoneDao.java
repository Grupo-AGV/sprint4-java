package com.penaestrada.dao;

import com.penaestrada.model.Telefone;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.util.List;

public interface TelefoneDao {

        void create(Telefone telefone, Connection connection);

        List<Telefone> findAll(Usuario usuario);

        void update(Telefone telefone, Connection connection);

        void deleteById(Long id, Connection connection);
}
