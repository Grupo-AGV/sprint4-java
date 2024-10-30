package com.penaestrada.model;

public class EnderecoUsuario {

    private Cliente cliente;
    private Integer numeroLogradouro;
    private String complementoNumero;
    private String pontoReferencia;


    public EnderecoUsuario(Cliente cliente, Integer numeroLogradouro, String complementoNumero, String pontoReferencia) {
        this.cliente = cliente;
        this.numeroLogradouro = numeroLogradouro;
        this.complementoNumero = complementoNumero;
        this.pontoReferencia = pontoReferencia;
    }

    public EnderecoUsuario() {
    }

    public Cliente getUsuario() {
        return cliente;
    }

    public void setUsuario(Cliente cliente) {
        this.cliente = cliente;
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
}
