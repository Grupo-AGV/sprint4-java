package com.penaestrada.model;

public class Peca {
    private String nome;
    private final String modelo;

    public Peca(String nome, String modelo) {
        this.nome = nome;
        this.modelo = modelo;
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
