package com.penaestrada.dao;

import com.penaestrada.infra.exceptions.ExclusaoTelefoneUnico;
import com.penaestrada.infra.exceptions.TelefoneNotFound;
import com.penaestrada.model.Telefone;
import com.penaestrada.model.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface TelefoneDao {

        void create(Telefone telefone) throws SQLException;

        List<Telefone> findAll(Usuario usuario);

        void update(Telefone telefone);

        void deleteByIdEUsuarioId(Long idCliente, Long idTelefone) throws SQLException, ExclusaoTelefoneUnico, TelefoneNotFound;
}
