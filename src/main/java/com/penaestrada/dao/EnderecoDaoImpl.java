package com.penaestrada.dao;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.model.Endereco;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class EnderecoDaoImpl implements EnderecoDao{

    @Override
    public void create(Endereco endereco, Connection connection) throws SQLException {
        String sql = "INSERT INTO T_PE_ENDERECO(id_usuario, nm_logradouro, nr_logradouro, ds_ponto_referencia, nm_cidade, nm_estado, nm_bairro, nm_zona_bairro, sq_cep) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setLong(1, endereco.getUsuario().getId());
        pstmt.setString(2, endereco.getNome());
        pstmt.setInt(3, endereco.getNumero());
        pstmt.setString(4, endereco.getPontoReferencia());
        pstmt.setString(5, endereco.getCidade());
        pstmt.setString(6, endereco.getEstado());
        pstmt.setString(7, endereco.getBairro());
        pstmt.setString(8, endereco.getZonaBairro());
        pstmt.setString(9, endereco.getCep());
        pstmt.executeUpdate();
    }

    @Override
    public List<Endereco> findEnderecoByUsuario(Usuario usuario) throws SQLException {
        List<Endereco> retorno = new ArrayList<>();
        String sql = "SELECT * FROM T_PE_ENDERECO WHERE id_usuario = ?";
        try (Connection connection = DatabaseConnectionFactory.create()) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, usuario.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Endereco endereco = new Endereco(
                        usuario,
                        rs.getString("nm_logradouro"),
                        rs.getInt("nr_logradouro"),
                        rs.getString("ds_ponto_referencia"),
                        rs.getString("nm_cidade"),
                        rs.getString("nm_estado"),
                        rs.getString("nm_bairro"),
                        rs.getString("nm_zona_bairro"),
                        rs.getString("sq_cep")
                );
                endereco.setIdEndereco(rs.getLong("id_endereco"));
                retorno.add(endereco);
            }
        }
        return retorno;
    }
}
