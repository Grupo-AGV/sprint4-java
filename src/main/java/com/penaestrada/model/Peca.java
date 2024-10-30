package com.penaestrada.model;

public class Peca {
    private final Long id;
    private String nome;
    private final String modelo;

    public Peca(Long id, String nome, String modelo) {
        this.id = id;
        this.nome = nome;
        this.modelo = modelo;
    }

    public Long getId() {
        return id;
    }

    public String getModelo() {
        return modelo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
