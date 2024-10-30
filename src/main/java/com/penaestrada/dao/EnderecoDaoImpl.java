package com.penaestrada.dao;

import com.penaestrada.model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class EnderecoDaoImpl implements EnderecoDao{

    @Override
    public void create(Endereco endereco, Connection connection) throws SQLException {
        String sql = "INSERT INTO T_PE_ENDERECO(id_usuario, nm_logradouro, nr_logradouro, ds_ponto_referencia, nm_cidade, nm_estado, nm_bairro, nm_zona_bairro, nr_cep) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setLong(1, endereco.getUsuario().getId());
        pstmt.setString(2, endereco.getNome());
        pstmt.setInt(3, endereco.getNumero());
        pstmt.setString(4, endereco.getPontoReferencia());
        pstmt.setString(5, endereco.getCidade());
        pstmt.setString(6, endereco.getEstado());
        pstmt.setString(7, endereco.getBairro());
        pstmt.setString(8, endereco.getZonaBairro());
        pstmt.setInt(9, endereco.getCep());
        pstmt.executeUpdate();
    }
}
