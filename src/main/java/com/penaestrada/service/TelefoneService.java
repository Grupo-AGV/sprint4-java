package com.penaestrada.service;

import com.penaestrada.dto.CriarTelefoneDto;
import com.penaestrada.dto.DetalhesTelefoneDto;
import com.penaestrada.infra.exceptions.ExclusaoTelefoneUnico;
import com.penaestrada.infra.exceptions.TelefoneNotFound;
import com.penaestrada.model.Telefone;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface TelefoneService {

    void criarTelefone(Usuario usuario, CriarTelefoneDto dto, Connection connection) throws SQLException;

    void adicionarTelefone(Usuario usuario, CriarTelefoneDto dto) throws SQLException;

    List<Telefone> buscarTelefonesPorUsuario(Usuario usuario) throws SQLException;

    void atualizarTelefone(Usuario usuario, Long id, CriarTelefoneDto dto) throws SQLException, TelefoneNotFound;

    void removeTelefoneDoUsuario(Usuario usuario, Long id) throws SQLException, TelefoneNotFound, ExclusaoTelefoneUnico;

    List<DetalhesTelefoneDto> mapearTelefones(List<Telefone> telefones);
}
