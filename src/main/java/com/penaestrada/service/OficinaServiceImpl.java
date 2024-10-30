package com.penaestrada.service;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dao.OficinaDao;
import com.penaestrada.dao.OficinaDaoFactory;
import com.penaestrada.dto.CriarEndereco;
import com.penaestrada.dto.CriarOficina;
import com.penaestrada.dto.DetalhesOficina;
import com.penaestrada.infra.exceptions.EmailExistente;
import com.penaestrada.model.Cargo;
import com.penaestrada.model.Endereco;
import com.penaestrada.model.Oficina;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class OficinaServiceImpl implements OficinaService {

    private final OficinaDao dao = OficinaDaoFactory.create();

    private final UsuarioService usuarioService = UsuarioServiceFactory.create();
    private final EnderecoService enderecoService = EnderecoServiceFactory.create();
    private final TelefoneService telefoneService = TelefoneServiceFactory.create();

    @Override
    public void create(CriarOficina dto) throws SQLException, EmailExistente {
        Connection connection = DatabaseConnectionFactory.create();
        try {
            Oficina oficina = new Oficina(dto.name(), dto.legalName(), dto.rating(), dto.mapsUrl(), 'A', dto.login().email(), dto.login().password(), Cargo.OFICINA);
            usuarioService.create(oficina, connection);

            dao.create(oficina, connection);

            enderecoService.criarEndereco(oficina, dto.address(), connection);
            telefoneService.criarTelefone(oficina, dto.contact(), connection);
            connection.commit();
        } catch (EmailExistente | SQLException e) {
            connection.rollback();
            throw e;
        }

    }

    @Override
    public List<DetalhesOficina> listarOficinas() throws SQLException {
        List<Oficina> oficinas = dao.findAll();
        return oficinas.stream().map(this::mapearOficina).toList();

    }

    @Override
    public DetalhesOficina detalhesOficinaPorUsuario(Usuario usuario) throws SQLException {
        return mapearOficina(dao.findByUsuario(usuario));
    }

    private DetalhesOficina mapearOficina(Oficina oficina) {
        return new DetalhesOficina(
                oficina.getIdOficina(), oficina.getNome(),
                enderecoService.mapearEnderecos(oficina.getEnderecos()),
                oficina.getAvaliacao(), oficina.getUrlMaps(),
                telefoneService.mapearTelefones(oficina.getTelefones()));
    }
}
