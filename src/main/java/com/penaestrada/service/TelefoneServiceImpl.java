package com.penaestrada.service;

import com.penaestrada.dao.TelefoneDao;
import com.penaestrada.dao.TelefoneDaoFactory;
import com.penaestrada.dto.CriarTelefone;
import com.penaestrada.infra.exceptions.ExclusaoTelefoneUnico;
import com.penaestrada.infra.exceptions.TelefoneNotFound;
import com.penaestrada.model.Telefone;
import com.penaestrada.model.Usuario;

import java.sql.SQLException;
import java.util.List;

public class TelefoneServiceImpl implements TelefoneService {

    private final TelefoneDao dao = TelefoneDaoFactory.create();

    @Override
    public void criarTelefone(Usuario usuario, CriarTelefone dto) throws SQLException {
        Telefone telefone = formatarCampos(dto);
        telefone.setUsuario(usuario);
        dao.create(telefone);
    }

    @Override
    public List<Telefone> buscarTelefonesPorUsuario(Usuario usuario) {
        return dao.findAll(usuario);
    }

    @Override
    public void atualizarTelefone(Usuario usuario, Long id, CriarTelefone dto) {
        Telefone telefone = formatarCampos(dto);
        telefone.setUsuario(usuario);
        telefone.setId(id);
        dao.update(telefone);
    }

    @Override
    public void removeTelefoneDoUsuario(Usuario usuario, Long id) throws SQLException, TelefoneNotFound, ExclusaoTelefoneUnico {
        dao.deleteByIdEUsuarioId(usuario.getId(), id);
    }

    private Telefone formatarCampos(CriarTelefone dto) {
        Integer ddi = Integer.parseInt(dto.ddi().replace("(", "").replace(")", ""));
        Integer ddd = Integer.parseInt(dto.ddd().replace("+", ""));
        Integer number = Integer.parseInt(dto.number().replace("-", ""));
        return new Telefone(null, ddi, ddd, number);
    }
}
