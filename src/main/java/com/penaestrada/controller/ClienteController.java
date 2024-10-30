package com.penaestrada.controller;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dto.ClienteDashboardDto;
import com.penaestrada.dto.CriarClienteDto;
import com.penaestrada.dto.CriarVeiculoDto;
import com.penaestrada.infra.exceptions.*;
import com.penaestrada.model.Cargo;
import com.penaestrada.model.Cliente;
import com.penaestrada.model.Veiculo;
import com.penaestrada.service.*;
import com.penaestrada.service.security.TokenService;
import com.penaestrada.service.security.TokenServiceFactory;

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
    private final TokenService tokenService = TokenServiceFactory.create();

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(CriarClienteDto dto) {
        try (Connection connection = DatabaseConnectionFactory.create()) {
            connection.setAutoCommit(false);
            Cliente cliente = new Cliente(dto.name(), dto.cpf(), dto.birthDate(), dto.login().email(), dto.login().password(), Cargo.CLIENTE);
            usuarioService.create(cliente, connection);
            clienteService.create(cliente, connection);

            CriarVeiculoDto veiculoDto = dto.vehicle();
            Veiculo veiculo = new Veiculo(cliente, veiculoDto.brand(), veiculoDto.model(), veiculoDto.licensePlate(), Integer.valueOf(veiculoDto.year()));
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

    @GET
    @Path("/dashboard")
    @Produces(MediaType.APPLICATION_JSON)
    public Response dashboard(@CookieParam("pe_access_token") String token) {
        try (Connection connection = DatabaseConnectionFactory.create()) {
            String login = tokenService.getSubject(token);
            ClienteDashboardDto dashboard = clienteService.dashboard(login, connection);
            return Response.ok().entity(dashboard).build();
        } catch (ClienteNotFound | CpfInvalido e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("/vehicle")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createVehicle(@CookieParam("pe_access_token") String token, CriarVeiculoDto dto) {
        try (Connection connection = DatabaseConnectionFactory.create()) {
            String login = tokenService.getSubject(token);
            Cliente cliente = (Cliente) usuarioService.findByLogin(login, connection);
            Veiculo veiculo = new Veiculo(cliente, dto.brand(), dto.model(), dto.licensePlate(), Integer.valueOf(dto.year()));
            veiculoService.create(veiculo, connection);
            connection.commit();
            return Response.status(Response.Status.CREATED).build();
        } catch (VeiculoExistente e) {
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
