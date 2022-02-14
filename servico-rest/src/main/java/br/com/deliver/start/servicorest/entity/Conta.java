package br.com.deliver.start.servicorest.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class Conta {
    @Id
    @GeneratedValue//(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotEmpty
    private String nome;

    private double valorOriginal;
    private double valorCorrigido;
    private LocalDate dVencimento;
    private LocalDate dPagamento;
    private int atraso;

    public Conta() {}

    public Conta(String nome, double valorOriginal, LocalDate dVencimento) {
        this.nome = nome;
        this.valorOriginal = valorOriginal;
        this.dVencimento = dVencimento;
        this.dPagamento = null;
        valorCorrigido = 0.0;
        atraso = 0;
    }

    public Conta(ContaReduzida contaReduzida) {
        if(contaReduzida.getId() != 0) this.id = contaReduzida.getId();
        this.nome = contaReduzida.getNome();
        this.valorOriginal = contaReduzida.getValorOriginal();
        this.dVencimento = contaReduzida.getDVencimento();
        this.dPagamento = null;
        valorCorrigido = 0.0;
        atraso = 0;
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
