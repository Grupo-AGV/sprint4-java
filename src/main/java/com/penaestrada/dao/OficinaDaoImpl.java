package com.penaestrada.dao;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OficinaDaoImpl implements OficinaDao {

    @Override
    public void create(Oficina oficina, Connection connection) throws SQLException {
        String sql = "INSERT INTO t_pe_oficina (id_usuario, nm_unid_oficina, nm_razao_social_oficina, vl_avaliacao, url_maps, st_oficina) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareCall(sql);
        pstmt.setLong(1, oficina.getId());
        pstmt.setString(2, oficina.getNome());
        pstmt.setString(3, oficina.getRazaoSocial());
        pstmt.setDouble(4, oficina.getAvaliacao());
        pstmt.setString(5, oficina.getUrlMaps());
        pstmt.setString(6, oficina.getStatus().toString());
        pstmt.executeUpdate();
    }

    @Override
    public List<Oficina> findAll() throws SQLException {
        List<Oficina> retorno = new ArrayList<>();
        Map<Long, Oficina> oficinaMap = new HashMap<>();
        String sql = "SELECT o.id_oficina, o.nm_unid_oficina, o.vl_avaliacao, o.url_maps, " +
                "e.id_endereco, e.nm_logradouro, e.nr_logradouro, " +
                "e.nm_cidade, e.nm_estado, e.nm_bairro, e.nm_zona_bairro, e.sq_cep, " +
                "t.id_telefone, t.nr_ddi, t.nr_ddd, t.nr_telefone " +
                "FROM t_pe_oficina o " +
                "LEFT JOIN t_pe_endereco e ON o.id_usuario = e.id_usuario " +
                "LEFT JOIN t_pe_telefone t ON o.id_usuario = t.id_usuario";

        try (Connection connection = DatabaseConnectionFactory.create();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                long idOficina = rs.getLong("id_oficina");
                Oficina oficina = oficinaMap.get(idOficina);

                // Se a oficina não existir, cria uma nova
                if (oficina == null) {
                    oficina = new Oficina(
                            rs.getString("nm_unid_oficina"),
                            null,
                            rs.getDouble("vl_avaliacao"),
                            rs.getString("url_maps"),
                            null,
                            null,
                            null,
                            Cargo.OFICINA
                    );
                    oficina.setIdOficina(idOficina);
                    oficinaMap.put(idOficina, oficina);
                    retorno.add(oficina);
                }

                adicionarEndereco(oficina, rs);
                adicionarTelefone(oficina, rs);
            }
        }
        return retorno;
    }

    @Override
    public Oficina findByUsuario(Usuario usuario) throws SQLException {
        Oficina oficina = null;

        String sql = "SELECT o.id_oficina, o.nm_unid_oficina, o.vl_avaliacao, o.url_maps, " +
                "e.id_endereco, e.nm_logradouro, e.nr_logradouro, " +
                "e.nm_cidade, e.nm_estado, e.nm_bairro, e.nm_zona_bairro, e.sq_cep, " +
                "t.id_telefone, t.nr_ddi, t.nr_ddd, t.nr_telefone " +
                "FROM t_pe_oficina o " +
                "LEFT JOIN t_pe_endereco e ON o.id_usuario = e.id_usuario " +
                "LEFT JOIN t_pe_telefone t ON o.id_usuario = t.id_usuario " +
                "WHERE o.id_usuario = ?";

        try (Connection connection = DatabaseConnectionFactory.create();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setLong(1, usuario.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                if (oficina == null) {
                    oficina = new Oficina(
                            rs.getString("nm_unid_oficina"),
                            null,
                            rs.getDouble("vl_avaliacao"),
                            rs.getString("url_maps"),
                            null,
                            null,
                            null,
                            Cargo.OFICINA
                    );
                    oficina.setIdOficina(rs.getLong("id_oficina"));
                }
                adicionarEndereco(oficina, rs);
                adicionarTelefone(oficina, rs);
            }
        }
        return oficina;
    }


    private void adicionarEndereco(Oficina oficina, ResultSet rs) throws SQLException {
        long idEndereco = rs.getLong("id_endereco");
        if (idEndereco != 0) {
            boolean enderecoExistente = oficina.getEnderecos().stream()
                    .anyMatch(e -> e.getIdEndereco() == idEndereco);

            if (!enderecoExistente) {
                // SEM SET DE PONTO DE REFENRENCIA
                Endereco endereco = new Endereco();
                endereco.setIdEndereco(idEndereco);
                endereco.setNome(rs.getString("nm_logradouro"));
                endereco.setNumero(rs.getInt("nr_logradouro"));
                endereco.setCidade(rs.getString("nm_cidade"));
                endereco.setEstado(rs.getString("nm_estado"));
                endereco.setBairro(rs.getString("nm_bairro"));
                endereco.setZonaBairro(rs.getString("nm_zona_bairro"));
                endereco.setCep(rs.getString("sq_cep"));
                oficina.getEnderecos().add(endereco);
            }
        }
    }

    private void adicionarTelefone(Oficina oficina, ResultSet rs) throws SQLException {
        long idTelefone = rs.getLong("id_telefone");
        if (idTelefone != 0) {
            boolean telefoneExistente = oficina.getTelefones().stream()
                    .anyMatch(t -> t.getId() == idTelefone);

            if (!telefoneExistente) {
                Telefone telefone = new Telefone(oficina, rs.getInt("nr_ddi"), rs.getInt("nr_ddd"), rs.getInt("nr_telefone"));
                telefone.setId(idTelefone);
                oficina.getTelefones().add(telefone);
            }
        }
    }

}
