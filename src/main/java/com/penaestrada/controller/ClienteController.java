package com.penaestrada.controller;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dto.CriarClienteDto;
import com.penaestrada.dto.CriarVeiculoDto;
import com.penaestrada.infra.exceptions.*;
import com.penaestrada.model.Cargo;
import com.penaestrada.model.Cliente;
import com.penaestrada.model.Veiculo;
import com.penaestrada.service.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Path("/client")
public class ClienteController {

    private final ClienteService clienteService = ClienteServiceFactory.create();
    private final UsuarioService usuarioService = UsuarioServiceFactory.create();
    private final VeiculoService veiculoService = VeiculoServiceFactory.create();

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(CriarClienteDto dto) {
        try (Connection connection = DatabaseConnectionFactory.create()) {
            connection.setAutoCommit(false);
            Cliente cliente = new Cliente(dto.nome(), dto.cpf(), dto.dataNascimento(), dto.login().email(), dto.login().password(), Cargo.CLIENTE);
            usuarioService.create(cliente, connection);
            clienteService.create(cliente, connection);

            CriarVeiculoDto veiculoDto = dto.veiculo();
            Veiculo veiculo = new Veiculo(cliente, veiculoDto.marca(), veiculoDto.modelo(), veiculoDto.placa(), Integer.valueOf(veiculoDto.ano()));
            veiculoService.create(veiculo, connection);
            System.out.println("VEÍCULO CADASTRADO");

            connection.commit();
            return Response.status(Response.Status.CREATED).build();
        } catch (EmailExistente | VeiculoExistente | CpfInvalido | CpfExistente e) {
            return Response.status(Response.Status.CONFLICT).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            try (Connection connection = DatabaseConnectionFactory.create()) {
                connection.rollback();
            } catch (SQLException ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(Map.of("error", "Erro ao realizar rollback")).build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

}
