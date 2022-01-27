package br.com.deliver.start.servicorest.service;

import br.com.deliver.start.servicorest.entity.Conta;
import br.com.deliver.start.servicorest.entity.ContaReduzida;
import br.com.deliver.start.servicorest.exception.ExcecaoSolicitacaoIncorreta;
import br.com.deliver.start.servicorest.repository.Repositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServicoUsuario {

    @Autowired
    private Repositorio repositorio;
    //private List<Conta> contas;

    //Deletar uma conta
    public void deleta(int id){
        repositorio.delete(consultarConta(id));
    }

    //Inclusão de conta a pagar
    public void novaConta(){
        Conta conta = new Conta("Allan", 100,  LocalDate.of(2022, 1,11));
        repositorio.save(conta);
    }

    //Inclui conta reduzida vinda do Postman
    @Transactional
    public Conta salvarConta(ContaReduzida contaReduzida) {
        return repositorio.save(new Conta(contaReduzida));
    }

    //Substitui a informação atual por uma nova
    public void replace(ContaReduzida contaReduzida) {
        Conta contaSalva = consultarConta(contaReduzida.getId());
        Conta conta = new Conta(contaReduzida);
        conta.setId(contaSalva.getId());
        repositorio.save(conta);
    }

    //Calculo de juros
    public void calculoJuros(){
        LocalDate hoje = LocalDate.now();
        List<Conta> contas = repositorio.findAll();

        for (Conta conta : contas) {
            int atraso = conta.getDVencimento().compareTo(hoje);
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
        }
        repositorio.saveAll(contas);
    }

    //Listagem das contas cadastradas em pagina
    public Page<Conta> listaTodosP(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    //Listagem das contas cadastradas em lista
    public List<Conta> listaTodosL() {
        return repositorio.findAll();
    }

    //Consulta uma conta
    public Conta consultarConta(int id){
        return repositorio.findById(id)
        .orElseThrow(() -> new ExcecaoSolicitacaoIncorreta("Conta não encontrada"));
    }
}
