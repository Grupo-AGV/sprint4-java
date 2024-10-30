package com.penaestrada.model;

public class Endereco {

    private Long idEndereco;
    private Usuario usuario;
    private String nome;
    private Integer numero;
    private String pontoReferencia;
    private String cidade;
    private String estado;
    private String bairro;
    private String zonaBairro;
    private Integer cep;

    public Endereco(Usuario usuario, String nome, Integer numero, String pontoReferencia, String cidade, String estado, String bairro, String zonaBairro, Integer cep) {
        this.usuario = usuario;
        this.nome = nome;
        this.numero = numero;
        this.pontoReferencia = pontoReferencia;
        this.cidade = cidade;
        this.estado = estado;
        this.bairro = bairro;
        this.zonaBairro = zonaBairro;
        this.cep = cep;
    }

    public Endereco() {
    }

    public Long getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(Long idEndereco) {
        this.idEndereco = idEndereco;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    public void setPontoReferencia(String pontoReferencia) {
        this.pontoReferencia = pontoReferencia;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getZonaBairro() {
        return zonaBairro;
    }

    public void setZonaBairro(String zonaBairro) {
        this.zonaBairro = zonaBairro;
    }

    public Integer getCep() {
        return cep;
    }

    public void setCep(Integer cep) {
        this.cep = cep;
    }
}
