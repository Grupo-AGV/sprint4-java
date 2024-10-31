package com.penaestrada.service;

import com.penaestrada.dto.CriarOficinaDto;
import com.penaestrada.dto.DetalhesOficinaDto;
import com.penaestrada.infra.exceptions.EmailExistente;
import com.penaestrada.infra.security.OficinaNotFound;
import com.penaestrada.model.Oficina;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OficinaService {

    void create(CriarOficinaDto dto) throws SQLException, EmailExistente;

    List<DetalhesOficinaDto> listarOficinas() throws SQLException;

    DetalhesOficinaDto detalhesOficinaPorUsuario(Usuario usuario) throws SQLException, OficinaNotFound;

    DetalhesOficinaDto detalhesOficinaPorId(Long id, Connection connection) throws SQLException, OficinaNotFound;

    Oficina findById(Long id, Connection connection) throws OficinaNotFound, SQLException;
}
