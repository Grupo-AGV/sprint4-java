package com.penaestrada.service;

import com.penaestrada.dto.CriarOficina;
import com.penaestrada.dto.DetalhesOficina;
import com.penaestrada.infra.exceptions.EmailExistente;
import com.penaestrada.model.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface OficinaService {

    void create(CriarOficina dto) throws SQLException, EmailExistente;

    List<DetalhesOficina> listarOficinas() throws SQLException;

    DetalhesOficina detalhesOficinaPorUsuario(Usuario usuario) throws SQLException;
}
