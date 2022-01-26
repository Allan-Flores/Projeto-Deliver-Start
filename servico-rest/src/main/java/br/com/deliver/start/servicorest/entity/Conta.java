package br.com.deliver.start.servicorest.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Entity
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotEmpty
    private String nome;

    private double valorOriginal;
    private double valorCorrigido;
    private LocalDate dVencimento;
    private LocalDate dPagamento;
    private int atraso;

    public Conta() {
    }

    public Conta(String nome, double valorOriginal, LocalDate dVencimento) {
        this.nome = nome;
        this.valorOriginal = valorOriginal;
        this.dVencimento = dVencimento;
        this.dPagamento = null;
        valorCorrigido = 0.0;
        atraso = 0;
    }

//    public Conta(String nome, double valorOriginal, String dVencimento) {
//        this.nome = nome;
//        this.valorOriginal = valorOriginal;
//        this.dVencimento = LocalDate.parse(dVencimento, formatter);
//        this.dPagamento = null;
//        valorCorrigido = 0.0;
//        atraso = 0;
//    }

    public Conta(ContaReduzida contaReduzida) {
        if(contaReduzida.getId() != 0) this.id = contaReduzida.getId();
        this.nome = contaReduzida.getNome();
        this.valorOriginal = contaReduzida.getValorOriginal();
        this.dVencimento = contaReduzida.getDVencimento();
        this.dPagamento = null;
        valorCorrigido = 0.0;
        atraso = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValorOriginal() {
        return valorOriginal;
    }

    public void setValorOriginal(Double valorOriginal) {
        this.valorOriginal = valorOriginal;
    }

    public double getValorCorrigido() {
        return valorCorrigido;
    }

    public void setValorCorrigido(double valorCorrigido) {
        this.valorCorrigido = valorCorrigido;
    }

    public LocalDate getdVencimento() {
        return dVencimento;
    }

    public void setdVencimento(LocalDate dVencimento) {
        this.dVencimento = dVencimento;
    }

    public LocalDate getdPagamento() {
        return dPagamento;
    }

    public void setdPagamento(LocalDate dPagamento) {
        this.dPagamento = dPagamento;
    }

    public int getAtraso() {
        return atraso;
    }

    public void setAtraso(int atraso) {
        this.atraso = atraso;
    }

    @Override
    public String toString() {
        return "======CONTA======" +
                "\nCliente= " + id + " - " + nome +
                "\nValor Original= " + valorOriginal +
                "\nValor Corrigido= " + valorCorrigido +
                "\nDia do Vencimento= " + dVencimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                "\nDia do Pagamento= " + dPagamento +
                "\nDias em Atraso= " + atraso +
                "\n============================";
    }
}
