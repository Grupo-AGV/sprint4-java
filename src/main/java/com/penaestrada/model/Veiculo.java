package com.penaestrada.model;

public class Veiculo {
    private Long id;
    private Cliente cliente;
    private String marca, modelo, placa;
    private Integer anoLancamento;

    public Veiculo(Cliente cliente, String marca, String modelo, String placa, Integer anoLancamento) {
        this.cliente = cliente;
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.anoLancamento = anoLancamento;
    }


    public void setAnoLancamento(Integer ano_lancamento) {
        this.anoLancamento = ano_lancamento;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Integer getAnoLancamento() {
        return anoLancamento;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public Cliente getCliente() {
        return cliente;
    }
}
