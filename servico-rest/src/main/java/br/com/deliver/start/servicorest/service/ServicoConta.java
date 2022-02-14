package br.com.deliver.start.servicorest.service;

import br.com.deliver.start.servicorest.entity.Conta;
import br.com.deliver.start.servicorest.entity.ContaReduzida;
import br.com.deliver.start.servicorest.exception.ExcecaoSolicitacaoIncorreta;
import br.com.deliver.start.servicorest.repository.RepositorioConta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ServicoConta {

    @Autowired
    private RepositorioConta repositorioConta;
    //private List<Conta> contas;

    //Deletar uma conta
    public void deleta(int id) {
        repositorioConta.delete(consultarConta(id));
    }

    //Inclusão de conta a pagar
    public void novaConta() {
        Conta conta = new Conta("Allan", 100, LocalDate.of(2022, 1, 11));
        repositorioConta.save(conta);
    }

    //Inclui conta reduzida vinda do Postman
    @Transactional
    public Conta salvarConta(ContaReduzida contaReduzida) {
        List<Conta> print = repositorioConta.findAll();
        Conta save = repositorioConta.save(new Conta(contaReduzida));
        print = repositorioConta.findAll();
        return save;
    }

    //Substitui a informação atual por uma nova
    public void replace(ContaReduzida contaReduzida) {
        Conta contaSalva = consultarConta(contaReduzida.getId());
        Conta conta = new Conta(contaReduzida);
        conta.setId(contaSalva.getId());
        repositorioConta.save(conta);
    }

    //Calculo de juros
    public void calculoJuros() {
        LocalDate hoje = LocalDate.now();
        List<Conta> contas = repositorioConta.findAll();

        for (Conta conta : contas) {
            int atraso = Math.max(((int) ChronoUnit.DAYS.between(conta.getDVencimento(), hoje)), 0);
            conta.setAtraso(atraso);

            double valorOriginal = conta.getValorOriginal();

            if (atraso > 0 && atraso <= 3) {
                conta.setValorCorrigido(
                        (0.02 * valorOriginal)
                        + ((valorOriginal * 0.001) * atraso)
                        + valorOriginal);
            } else if (atraso > 3 && atraso <= 5) {
                conta.setValorCorrigido(
                        (0.03 * valorOriginal)
                        + ((valorOriginal * 0.002) * atraso)
                        + valorOriginal);
            } else if (atraso > 5) {
                conta.setValorCorrigido(
                        (0.05 * valorOriginal)
                        + ((valorOriginal * 0.003) * atraso)
                        + valorOriginal);
            }
        }
        repositorioConta.saveAll(contas);
    }

    //Listagem das contas cadastradas em pagina
    public Page<Conta> listaTodosP(Pageable pageable) {
        return repositorioConta.findAll(pageable);
    }

    //Listagem das contas cadastradas em lista
    public List<Conta> listaTodosL() {
        return repositorioConta.findAll();
    }

    //Consulta uma conta
    public Conta consultarConta(int id) {
        return repositorioConta.findById(id)
                .orElseThrow(() -> new ExcecaoSolicitacaoIncorreta("Conta não encontrada"));
    }
}
