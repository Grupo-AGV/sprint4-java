package com.penaestrada.dao;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.infra.security.OficinaNotFound;
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
        String sql = "INSERT INTO t_pe_oficina (id_oficina, nm_razao_social_oficina, vl_avaliacao, url_maps, st_oficina) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareCall(sql)) {
            pstmt.setLong(1, oficina.getId());
            pstmt.setString(2, oficina.getRazaoSocial());
            pstmt.setDouble(3, oficina.getAvaliacao());
            pstmt.setString(4, oficina.getUrlMaps());
            pstmt.setString(5, oficina.getStatus().toString());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Oficina> findAll() throws SQLException {
        List<Oficina> retorno = new ArrayList<>();
        Map<Long, Oficina> oficinaMap = new HashMap<>();
        String sql = "SELECT o.id_oficina, u.nm_usuario, o.vl_avaliacao, o.url_maps, " +
                "e.id_endereco, e.nm_logradouro, e.nr_logradouro, " +
                "e.nm_cidade, e.nm_estado, e.nm_bairro, e.nm_zona_bairro, e.sq_cep, " +
                "t.id_telefone, t.nr_ddi, t.nr_ddd, t.nr_telefone " +
                "FROM t_pe_oficina o " +
                "LEFT JOIN t_pe_usuario u ON o.id_oficina = u.id_usuario " +
                "LEFT JOIN t_pe_endereco e ON o.id_oficina = e.id_usuario " +
                "LEFT JOIN t_pe_telefone t ON o.id_oficina = t.id_usuario";

        try (Connection connection = DatabaseConnectionFactory.create();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id_oficina");
                Oficina oficina = oficinaMap.get(id);

                // Se a oficina não existir, cria uma nova
                if (oficina == null) {
                    // campos nulos pois nao afeta a regra de negocio definida
                    oficina = new Oficina(
                            rs.getString("nm_usuario"),
                            null,
                            rs.getDouble("vl_avaliacao"),
                            rs.getString("url_maps"),
                            null,
                            null,
                            null,
                            Cargo.OFICINA
                    );
                    oficina.setId(id);
                    oficinaMap.put(id, oficina);
                    retorno.add(oficina);
                }

                adicionarEndereco(oficina, rs);
                adicionarTelefone(oficina, rs);
            }
        }
        return retorno;
    }

    @Override
    public Oficina findById(Long id, Connection connection) throws SQLException, OficinaNotFound {
        String sql = "SELECT o.id_oficina, u.nm_usuario, o.vl_avaliacao, o.url_maps, o.st_oficina, " +
                "e.id_endereco, e.nm_logradouro, e.nr_logradouro, " +
                "e.nm_cidade, e.nm_estado, e.nm_bairro, e.nm_zona_bairro, e.sq_cep, " +
                "t.id_telefone, t.nr_ddi, t.nr_ddd, t.nr_telefone " +
                "FROM t_pe_oficina o " +
                "LEFT JOIN t_pe_usuario u ON o.id_oficina = u.id_usuario " +
                "LEFT JOIN t_pe_endereco e ON o.id_oficina = e.id_usuario " +
                "LEFT JOIN t_pe_telefone t ON o.id_oficina = t.id_usuario " +
                "WHERE o.id_oficina = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            System.out.println("passou aqui");
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Oficina oficina = new Oficina(
                        rs.getString("nm_usuario"),
                        null,
                        rs.getDouble("vl_avaliacao"),
                        rs.getString("url_maps"),
                        rs.getString("st_oficina").toCharArray()[0],
                        null,
                        null,
                        Cargo.OFICINA
                );
                oficina.setId(rs.getLong("id_oficina"));

                do {
                    adicionarEndereco(oficina, rs);
                    adicionarTelefone(oficina, rs);
                } while (rs.next());
                return oficina;
            } else {
                throw new OficinaNotFound("Oficina não encontrada.");
            }
        }
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
