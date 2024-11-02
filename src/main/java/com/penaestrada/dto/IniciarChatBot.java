package com.penaestrada.dto;

import java.util.List;

public record IniciarChatBot(
        String username,
        List<ResponseSimples> vehicles,
        List<ResponseSimples> workshops
) {
}

