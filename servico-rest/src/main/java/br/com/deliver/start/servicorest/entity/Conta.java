package br.com.deliver.start.servicorest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
public class Conta {

    @Id
    @GeneratedValue
    private int id;
    @Column
    private String nome;
    @Column
    private double valorOriginal;
    @Column
    private double valorCorrigido;
    @Column
    private LocalDate dVencimento;
    @Column
    private LocalDate dPagamento;
    @Column
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
