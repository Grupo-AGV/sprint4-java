package com.penaestrada.model;

public enum Cargo {
    CLIENTE("CLIENTE"),
    OFICINA("OFICINA");

    private final String descricao;

    Cargo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
