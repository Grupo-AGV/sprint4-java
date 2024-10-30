package com.penaestrada.service;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dao.UsuarioDao;
import com.penaestrada.dao.UsuarioDaoFactory;
import com.penaestrada.infra.exceptions.EmailExistente;
import com.penaestrada.infra.exceptions.LoginInvalido;
import com.penaestrada.infra.exceptions.LoginNotFound;
import com.penaestrada.infra.exceptions.UsuarioInvalidoException;
import com.penaestrada.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;

class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDao dao = UsuarioDaoFactory.create();

    @Override
    public void create(Usuario usuario, Connection connection) throws SQLException, EmailExistente {
        if (usuario.getEmail().isBlank() || usuario.getSenha().isBlank()) {
            throw new UsuarioInvalidoException();
        }
        try {
            dao.findByEmail(usuario.getEmail(), connection);
            throw new EmailExistente("Email já cadastrado. Tente outro email.");
        } catch (LoginNotFound e) {
            // Email não existe, segue o fluxo
        }

        String senhaEncriptada = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
        usuario.setSenha(senhaEncriptada);
        dao.create(usuario, connection);
    }

    @Override
    public Usuario logarUsuario(String email, String senha) throws SQLException, LoginInvalido, LoginNotFound {
        try (Connection connection = DatabaseConnectionFactory.create()){
            Usuario usuario = dao.findByEmail(email, connection);
            if (usuario != null && BCrypt.checkpw(senha, usuario.getSenha()))
                return usuario;

            throw new LoginInvalido("Usuário ou senha inválidos. Verifique suas credênciais.");
        }
    }

    @Override
    public Usuario findByLogin(String email) throws SQLException, LoginNotFound {
        Connection connection = DatabaseConnectionFactory.create();
        return dao.findByEmail(email, connection);
    }
}
