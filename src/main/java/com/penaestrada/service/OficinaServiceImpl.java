package com.penaestrada.service;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dao.OficinaDao;
import com.penaestrada.dao.OficinaDaoFactory;
import com.penaestrada.dto.CriarOficinaDto;
import com.penaestrada.dto.DetalhesOficinaDto;
import com.penaestrada.infra.exceptions.EmailExistente;
import com.penaestrada.infra.exceptions.OficinaNotFound;
import com.penaestrada.model.Cargo;
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
    public void create(CriarOficinaDto dto) throws SQLException, EmailExistente {
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
    public List<DetalhesOficinaDto> listarOficinas() throws SQLException {
        List<Oficina> oficinas = dao.findAll();
        return oficinas.stream().map(this::mapearOficina).toList();

    }

    @Override
    public DetalhesOficinaDto detalhesOficinaPorUsuario(Usuario usuario) throws SQLException, OficinaNotFound {
        try (Connection connection = DatabaseConnectionFactory.create()) {
            return buscaDetalhesOficina(usuario.getId(), connection);
        }
    }

    @Override
    public DetalhesOficinaDto detalhesOficinaPorId(Long id, Connection connection) throws SQLException, OficinaNotFound {
        return buscaDetalhesOficina(id, connection);
    }

    @Override
    public Oficina findById(Long id, Connection connection) throws OficinaNotFound, SQLException {
        return dao.findById(id, connection);
    }

    private DetalhesOficinaDto buscaDetalhesOficina(Long id, Connection connection) throws SQLException, OficinaNotFound {
        Oficina oficina = dao.findById(id, connection);
        return mapearOficina(oficina);
    }

    private DetalhesOficinaDto mapearOficina(Oficina oficina) {
        return new DetalhesOficinaDto(
                oficina.getId(), oficina.getNome(),
                enderecoService.mapearEnderecos(oficina.getEnderecos()),
                oficina.getAvaliacao(), oficina.getUrlMaps(),
                telefoneService.mapearTelefones(oficina.getTelefones()));
    }
}
