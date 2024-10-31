package com.penaestrada.service;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dao.TelefoneDao;
import com.penaestrada.dao.TelefoneDaoFactory;
import com.penaestrada.dto.CriarTelefone;
import com.penaestrada.dto.DetalhesTelefone;
import com.penaestrada.infra.exceptions.ExclusaoTelefoneUnico;
import com.penaestrada.infra.exceptions.TelefoneNotFound;
import com.penaestrada.model.Telefone;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class TelefoneServiceImpl implements TelefoneService {

    private final TelefoneDao dao = TelefoneDaoFactory.create();


    @Override
    public void criarTelefone(Usuario usuario, CriarTelefone dto, Connection connection) throws SQLException {
        salvarTelefone(usuario, dto, connection);
    }

    @Override
    public void adicionarTelefone(Usuario usuario, CriarTelefone dto) throws SQLException {
        try (Connection connection = DatabaseConnectionFactory.create()){
            salvarTelefone(usuario, dto, connection);
        }
    }

    @Override
    public List<Telefone> buscarTelefonesPorUsuario(Usuario usuario) throws SQLException {
        return dao.findAll(usuario);
    }

    @Override
    public void atualizarTelefone(Usuario usuario, Long id, CriarTelefone dto) throws SQLException {
        Telefone telefone = formatarTelefone(usuario, dto);
        telefone.setId(id);
        dao.update(telefone);
    }

    @Override
    public void removeTelefoneDoUsuario(Usuario usuario, Long id) throws SQLException, TelefoneNotFound, ExclusaoTelefoneUnico {
        dao.deleteByIdEUsuarioId(usuario.getId(), id);
    }

    @Override
    public List<DetalhesTelefone> mapearTelefones(List<Telefone> telefones) {
        return telefones.stream().map(t -> new DetalhesTelefone(t.getId(), t.getNumeroCompleto())).toList();
    }

    private void salvarTelefone(Usuario usuario, CriarTelefone dto, Connection connection) throws SQLException {
        Telefone telefone = formatarTelefone(usuario, dto);
        dao.create(telefone, connection);
    }

    private Telefone formatarTelefone(Usuario usuario, CriarTelefone dto) {
        Telefone telefone = formatarCampos(dto);
        telefone.setUsuario(usuario);
        return telefone;
    }

    private Telefone formatarCampos(CriarTelefone dto) {
        Integer ddi = Integer.parseInt(dto.ddi().replace("(", "").replace(")", ""));
        Integer ddd = Integer.parseInt(dto.ddd().replace("+", ""));
        Integer number = Integer.parseInt(dto.number().replace("-", ""));
        return new Telefone(null, ddi, ddd, number);
    }
}
