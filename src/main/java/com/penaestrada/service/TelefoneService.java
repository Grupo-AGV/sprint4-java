package com.penaestrada.service;

import com.penaestrada.dto.CriarTelefone;
import com.penaestrada.infra.exceptions.ExclusaoTelefoneUnico;
import com.penaestrada.infra.exceptions.TelefoneNotFound;
import com.penaestrada.model.Telefone;
import com.penaestrada.model.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface TelefoneService {

    void criarTelefone(Usuario cliente, CriarTelefone dto) throws SQLException;

    List<Telefone> buscarTelefonesPorUsuario(Usuario usuario) throws SQLException;

    void atualizarTelefone(Usuario usuario, Long id, CriarTelefone dto);

    void removeTelefoneDoUsuario(Usuario usuario, Long id) throws SQLException, TelefoneNotFound, ExclusaoTelefoneUnico;
}
