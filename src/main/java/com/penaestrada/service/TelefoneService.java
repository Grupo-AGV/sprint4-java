package com.penaestrada.service;

import com.penaestrada.dto.CriarTelefone;
import com.penaestrada.dto.DetalhesTelefone;
import com.penaestrada.infra.exceptions.ExclusaoTelefoneUnico;
import com.penaestrada.infra.exceptions.TelefoneNotFound;
import com.penaestrada.model.Telefone;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface TelefoneService {

    void criarTelefone(Usuario usuario, CriarTelefone dto, Connection connection) throws SQLException;

    void adicionarTelefone(Usuario usuario, CriarTelefone dto) throws SQLException;

    List<Telefone> buscarTelefonesPorUsuario(Usuario usuario) throws SQLException;

    void atualizarTelefone(Usuario usuario, Long id, CriarTelefone dto) throws SQLException;

    void removeTelefoneDoUsuario(Usuario usuario, Long id) throws SQLException, TelefoneNotFound, ExclusaoTelefoneUnico;

    List<DetalhesTelefone> mapearTelefones(List<Telefone> telefones);
}
