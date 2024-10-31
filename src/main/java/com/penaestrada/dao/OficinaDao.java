package com.penaestrada.dao;

import com.penaestrada.infra.security.OficinaNotFound;
import com.penaestrada.model.Oficina;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OficinaDao {
    void create(Oficina oficina, Connection connection) throws SQLException;

    List<Oficina> findAll() throws SQLException;

    Oficina findById(Long id, Connection connection) throws SQLException, OficinaNotFound;
}
