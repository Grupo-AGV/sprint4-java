package com.penaestrada.model;

import java.util.ArrayList;
import java.util.List;

public class Oficina extends Usuario {
    private Long idOficina;
    private String nome;
    private String razaoSocial;
    private Double avaliacao;
    private String urlMaps;
    private Character status;

    List<Endereco> enderecos = new ArrayList<>();
    List<Telefone> telefones = new ArrayList<>();

    public Oficina(String email, String senha, Cargo cargo) {
        super(email, senha, cargo);
    }

    public Oficina(String nome, String razaoSocial, Double avaliacao, String urlMaps, Character status, String email, String senha, Cargo cargo) {
        super(email, senha, cargo);
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.avaliacao = avaliacao;
        this.urlMaps = urlMaps;
        this.status = status;
    }

    public Long getIdOficina() {
        return idOficina;
    }

    public void setIdOficina(Long idOficina) {
        this.idOficina = idOficina;
    }

    public String getNome() {
        return nome;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }


    public String getUrlMaps() {
        return urlMaps;
    }

    public Character getStatus() {
        return status;
    }


    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public List<Telefone> getTelefones() {
        return telefones;
    }
}
