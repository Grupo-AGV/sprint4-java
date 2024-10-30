package com.penaestrada.service;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dao.EnderecoDao;
import com.penaestrada.dao.EnderecoDaoFactory;
import com.penaestrada.dto.CriarEndereco;
import com.penaestrada.dto.DetalhesEndereco;
import com.penaestrada.model.Endereco;
import com.penaestrada.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EnderecoServiceImpl implements EnderecoService {

    private final EnderecoDao dao = EnderecoDaoFactory.create();

    @Override
    public void criarEndereco(Usuario usuario, CriarEndereco address, Connection connection) throws SQLException {
        salvarEndereco(address, usuario, connection);
    }

    @Override
    public void adicionarEndereco(Usuario usuario, CriarEndereco dto) throws SQLException {
        try (Connection connection = DatabaseConnectionFactory.create()){
            salvarEndereco(dto, usuario, connection);
        }
    }

    @Override
    public List<DetalhesEndereco> mapearEnderecos(List<Endereco> enderecos) {
        return enderecos.stream().map(this::mapearEndereco).toList();
    }

    private DetalhesEndereco mapearEndereco(Endereco endereco) {
        String cep = String.valueOf(endereco.getCep()).substring(0, 5) + "-" + String.valueOf(endereco.getCep()).substring(5);
        return new DetalhesEndereco(
                endereco.getIdEndereco(), endereco.getNome(), String.valueOf(endereco.getNumero()),
                endereco.getPontoReferencia(), endereco.getCidade(), endereco.getEstado(),
                endereco.getBairro(), endereco.getZonaBairro(), cep);
    }

    private void salvarEndereco(CriarEndereco dto, Usuario usuario, Connection connection) throws SQLException {
        Endereco endereco = new Endereco(
                usuario, dto.streetName(), Integer.parseInt(dto.number()),
                dto.referencePoint(), dto.city(), dto.state(),
                dto.neighborhood(), dto.neighborhoodZone(),
                Integer.parseInt(dto.zipCode().replace("-", "")));
        dao.create(endereco, connection);
    }
}
