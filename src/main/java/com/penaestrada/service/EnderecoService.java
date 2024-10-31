package com.penaestrada.service;

import com.penaestrada.dto.CriarEnderecoDto;
import com.penaestrada.dto.DetalhesEnderecoDto;
import com.penaestrada.model.Endereco;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface EnderecoService {

    void criarEndereco(Usuario usuario, CriarEnderecoDto dto, Connection connection) throws SQLException;

    void adicionarEndereco(Usuario usuario, CriarEnderecoDto dto) throws SQLException;

    List<DetalhesEnderecoDto> mapearEnderecos(List<Endereco> enderecos);

    List<DetalhesEnderecoDto> listarEnderecosPorUsuario(Usuario usuario) throws SQLException;
}
