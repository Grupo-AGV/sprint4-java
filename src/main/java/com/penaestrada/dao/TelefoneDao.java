package com.penaestrada.dao;

import com.penaestrada.infra.exceptions.ExclusaoTelefoneUnico;
import com.penaestrada.infra.exceptions.TelefoneNotFound;
import com.penaestrada.model.Telefone;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface TelefoneDao {

        void create(Telefone telefone, Connection connection) throws SQLException;

        List<Telefone> findAll(Usuario usuario) throws SQLException;

        void update(Telefone telefone) throws SQLException, TelefoneNotFound;

        void deleteByIdEUsuarioId(Long idCliente, Long idTelefone) throws SQLException, ExclusaoTelefoneUnico, TelefoneNotFound;
}
