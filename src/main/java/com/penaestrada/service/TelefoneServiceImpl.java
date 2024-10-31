package com.penaestrada.service;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dao.TelefoneDao;
import com.penaestrada.dao.TelefoneDaoFactory;
import com.penaestrada.dto.CriarTelefoneDto;
import com.penaestrada.dto.DetalhesTelefoneDto;
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
    public void criarTelefone(Usuario usuario, CriarTelefoneDto dto, Connection connection) throws SQLException {
        salvarTelefone(usuario, dto, connection);
    }

    @Override
    public void adicionarTelefone(Usuario usuario, CriarTelefoneDto dto) throws SQLException {
        try (Connection connection = DatabaseConnectionFactory.create()){
            salvarTelefone(usuario, dto, connection);
        }
    }

    @Override
    public List<Telefone> buscarTelefonesPorUsuario(Usuario usuario, Connection connection) throws SQLException {
        return dao.findAll(usuario, connection);
    }

    @Override
    public void atualizarTelefone(Usuario usuario, Long id, CriarTelefoneDto dto) throws SQLException, TelefoneNotFound {
        Telefone telefone = formatarTelefone(usuario, dto);
        telefone.setId(id);
        dao.update(telefone);
    }

    @Override
    public void removeTelefoneDoUsuario(Usuario usuario, Long id) throws SQLException, TelefoneNotFound, ExclusaoTelefoneUnico {
        dao.deleteByIdEUsuarioId(usuario.getId(), id);
    }

    @Override
    public List<DetalhesTelefoneDto> mapearTelefones(List<Telefone> telefones) {
        return telefones.stream().map(t -> new DetalhesTelefoneDto(t.getId(), t.getNumeroCompleto())).toList();
    }

    private void salvarTelefone(Usuario usuario, CriarTelefoneDto dto, Connection connection) throws SQLException {
        Telefone telefone = formatarTelefone(usuario, dto);
        dao.create(telefone, connection);
    }

    private Telefone formatarTelefone(Usuario usuario, CriarTelefoneDto dto) {
        Telefone telefone = formatarCampos(dto);
        telefone.setUsuario(usuario);
        return telefone;
    }

    private Telefone formatarCampos(CriarTelefoneDto dto) {
        Integer ddi = Integer.parseInt(dto.ddi().replace("(", "").replace(")", ""));
        Integer ddd = Integer.parseInt(dto.ddd().replace("+", ""));
        Integer number = Integer.parseInt(dto.number().replace("-", ""));
        return new Telefone(null, ddi, ddd, number);
    }
}
