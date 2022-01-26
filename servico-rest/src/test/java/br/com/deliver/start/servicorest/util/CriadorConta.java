package br.com.deliver.start.servicorest.util;

import br.com.deliver.start.servicorest.entity.Conta;

import java.time.LocalDate;

public class CriadorConta {
    public static Conta criarConta(){
        return new Conta("Alana", 500, LocalDate.of(2022, 1, 22));
    }

    public static Conta criarContaComId(){
        Conta conta = new Conta("Alana", 500, LocalDate.of(2022, 1, 22));
        conta.setId(1);
        return conta;
    }

    public static Conta criarConta2(){
        Conta conta = new Conta("Alana", 500, LocalDate.of(2022, 1, 22));
        conta.setId(1);
        return conta;
    }
}
