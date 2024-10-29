package com.penaestrada.model;

public class Oficina extends Usuario {
    private String nome;
    private String razaoSocial;
    private Integer numeroLogradouro;
    private String complementoNumero;
    private String pontoReferencia;
    private Character status;

    public Oficina(String email, String senha, Cargo cargo) {
        super(email, senha, cargo);
    }

    public Oficina(String nome, String razaoSocial, Character status, String email, String senha, Cargo cargo) {
        super(email, senha, cargo);
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public Integer getNumeroLogradouro() {
        return numeroLogradouro;
    }

    public void setNumeroLogradouro(Integer numeroLogradouro) {
        this.numeroLogradouro = numeroLogradouro;
    }

    public String getComplementoNumero() {
        return complementoNumero;
    }

    public void setComplementoNumero(String complementoNumero) {
        this.complementoNumero = complementoNumero;
    }

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    public void setPontoReferencia(String pontoReferencia) {
        this.pontoReferencia = pontoReferencia;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }


}
