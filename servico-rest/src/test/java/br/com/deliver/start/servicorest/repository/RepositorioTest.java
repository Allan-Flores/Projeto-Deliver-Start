//convenção para assinatura de metodos test: nomeDoMetodoTestado_OQueEleDeveFazer_Retorno
//teste esperando uma excecão; seguir a sequencia de videos
package br.com.deliver.start.servicorest.repository;

import br.com.deliver.start.servicorest.entity.Conta;
import br.com.deliver.start.servicorest.exception.ExcecaoSolicitacaoIncorreta;
import br.com.deliver.start.servicorest.util.CriadorConta;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@DisplayName("Teste do repositório")
class RepositorioTest {

    @Autowired
    private RepositorioConta repositorioConta;

    @Test
    @DisplayName("Criando uma Conta e salva uma conta")
    void save_salvarConta_quandoSucesso(){
        Conta conta = criarConta();
        Conta contaSalva = this.repositorioConta.save(conta);

        Assertions.assertThat(contaSalva).isNotNull();
        Assertions.assertThat(contaSalva.getId()).isNotNull();
        Assertions.assertThat(contaSalva.getNome()).isEqualTo(conta.getNome());
    }

    @Test
    @DisplayName("Atualizando uma Conta e salva uma conta")
    void atualizar_conta_quandoSucesso(){
        Conta conta = criarConta();
        Conta contaSalva = this.repositorioConta.save(conta);

        contaSalva.setNome("Orlando");
        Conta contaF5 = this.repositorioConta.save(contaSalva);

        Assertions.assertThat(contaF5).isNotNull();
        Assertions.assertThat(contaF5.getId()).isNotNull();
        Assertions.assertThat(contaF5.getNome()).isEqualTo(contaSalva.getNome());
    }

    @Test
    @DisplayName("Delete uma Conta e salva uma conta")
    void Delete_conta_quandoSucesso(){
        Conta conta = criarConta();
        Conta contaSalva = this.repositorioConta.save(conta);

        this.repositorioConta.delete(contaSalva);

        Optional<Conta> contaOptional = this.repositorioConta.findById(contaSalva.getId());

        Assertions.assertThat(contaOptional).isEmpty();
    }

    @Test
    @DisplayName("Quando a conta procurada existe")
    void findById_ProcurarConta_QuandoSucesso() {
        Conta conta = criarConta();
        Conta contaSalva = this.repositorioConta.save(conta);

        Optional<Conta> contaProcurada = repositorioConta.findById(contaSalva.getId());

        Assertions.assertThat(contaProcurada.get().getId()).isEqualTo(contaSalva.getId());
    }

    @Test
    @DisplayName("Excecão quando a conta procurada não existe")
    void findById_ProcurarConta_QuandoFalha() {
      Assertions.assertThatCode(() -> repositorioConta.findById(100))
                .doesNotThrowAnyException();
    }

    private Conta criarConta(){
        return CriadorConta.criarContaComId();
    }
}