package br.com.deliver.start.servicorest.service;

import br.com.deliver.start.servicorest.entity.Conta;
import br.com.deliver.start.servicorest.repository.Repositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServicoUsuario {

    @Autowired
    private Repositorio repositorio;

    public List<Conta> criarContas(){
        List<Conta> contas = new ArrayList<>();
        return contas;
    }

    public void deleta(){
        System.out.println("deletando...");
    }

    //Inclusão de conta a pagar
    public void novaConta(){
        Conta conta = new Conta("Allan", 100,  LocalDate.of(2022, 1,11));
        repositorio.save(conta);
        System.out.println("nova conta criada");
    }

    //Listagem das contas cadastradas
    public void contasCadastradas(){
        LocalDate hoje = LocalDate.now();
        List<Conta> contas = repositorio.findAll();

        for (Conta conta : contas) {
            int atraso = conta.getdVencimento().compareTo(hoje);
            conta.setAtraso(Math.abs(atraso));

                if (atraso < 0 && atraso >= -3) {
                conta.setValorCorrigido(
                        (0.02 * conta.getValorOriginal())
                        + ((conta.getValorOriginal() * 0.001) * Math.abs(atraso))
                        + conta.getValorOriginal());
            }else if (atraso < -3 && atraso >= -5){
                conta.setValorCorrigido(
                        (0.03 * conta.getValorOriginal())
                        + ((conta.getValorOriginal() * 0.002) * Math.abs(atraso))
                        + conta.getValorOriginal());
            }else if (atraso < -5){
                conta.setValorCorrigido(
                        (0.05 * conta.getValorOriginal())
                        + ((conta.getValorOriginal() * 0.003) * Math.abs(atraso))
                        + conta.getValorOriginal());
            }
            System.out.println(conta.toString());
        }
    }

    //    public Conta consultarConta(long id){
//        return repositorio.findById(id).get();
//    }
}
