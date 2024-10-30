package com.penaestrada.model;

import com.penaestrada.infra.exceptions.CpfInvalido;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {

    private String nome;
    private String cpf;
    private LocalDate dataNascimento;

    private List<Veiculo> veiculos = new ArrayList<>();

    public Cliente(String email, String senha, Cargo cargo) {
        super(email, senha, cargo);
    }

    public Cliente(String nome, String cpf, String dataNascimento, String email, String senha, Cargo cargo) throws CpfInvalido {
        super(email, senha, cargo);
        this.nome = nome;
        validaCpf(cpf);
        setDataNascimento(LocalDate.parse(dataNascimento));
    }

    private void setDataNascimento(LocalDate dataNascimento) {
        if (isMaiorDeIdade(dataNascimento)) {
            throw new RuntimeException("Usuário menor de idade!");
        } else {
            this.dataNascimento = dataNascimento;
        }
    }

    private boolean isMaiorDeIdade(LocalDate dataNascimento) {
        return dataNascimento.isAfter(LocalDate.now().minusYears(18));
    }

    private void validaCpf(String cpf) throws CpfInvalido {
        cpf = RemoveSpecialChar(cpf);
        if (!ValidateCPFString(cpf)) {
            String[] str = cpf.split("");
            List<Integer> lastTwoCPF = new ArrayList<>();
            int result1 = 0;
            int result2 = 0;

            // AJEITANDO ARRAY
            for (int i = str.length - 2; i < str.length; i++) {
                lastTwoCPF.add(Integer.parseInt(str[i]));
            }

            // CASO 1
            for (int i = 0; i < str.length - 2; i++) {
                result1 += Integer.parseInt(str[i]) * (i + 1);
            }

            // CASO 2
            for (int i = 0; i < str.length - 1; i++) {
                result2 += Integer.parseInt(str[i]) * i;
            }

            // VALIDACAO FINAL
            int digit1 = ValidateResult(result1);
            int digit2 = ValidateResult(result2);

            if (ValidateCPFDigits(lastTwoCPF, digit1, digit2)) {
                this.cpf = cpf;
            } else {
                throw new CpfInvalido("CPF inválido!");
            }
        } else {
            throw new CpfInvalido("CPF inválido!");
        }
    }

    private Integer ValidateResult(Integer result) {
        return result % 11 == 10 ? 0 : result % 11;
    }

    private boolean ValidateCPFDigits(List<Integer> lastTwo, int d1, int d2) {
        return lastTwo.contains(d1) && lastTwo.contains(d2);
    }

    private boolean ValidateCPFString(String cpf) {
        return cpf.equals("00000000000") || cpf.equals("11111111111") || cpf.equals("22222222222") ||
                cpf.equals("33333333333") || cpf.equals("44444444444") || cpf.equals("55555555555") ||
                cpf.equals("66666666666") || cpf.equals("77777777777") || cpf.equals("88888888888") ||
                cpf.equals("99999999999") || (cpf.length() != 11);
    }

    private String RemoveSpecialChar(String doc) {
        if (doc.contains(".")) {
            doc = doc.replace(".", "");
        }
        if (doc.contains("-")) {
            doc = doc.replace("-", "");
        }
        if (doc.contains("/")) {
            doc = doc.replace("/", "");
        }
        return doc;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + getId() +
                ", name='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", birthDate=" + dataNascimento +
                '}';
    }
}
