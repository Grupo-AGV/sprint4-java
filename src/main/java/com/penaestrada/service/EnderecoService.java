package com.penaestrada.service;

import com.penaestrada.dto.CriarEndereco;
import com.penaestrada.dto.DetalhesEndereco;
import com.penaestrada.model.Endereco;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface EnderecoService {

    void criarEndereco(Usuario usuario, CriarEndereco dto, Connection connection) throws SQLException;

    void adicionarEndereco(Usuario usuario, CriarEndereco dto) throws SQLException;

    List<DetalhesEndereco> mapearEnderecos(List<Endereco> enderecos);

    List<DetalhesEndereco> listarEnderecosPorUsuario(Usuario usuario) throws SQLException;
}
