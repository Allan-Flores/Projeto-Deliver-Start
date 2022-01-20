package br.com.deliver.start.servicorest.repository;

import br.com.deliver.start.servicorest.entity.Conta;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

@DataJpaTest
@DisplayName("Teste do repositório")
class RepositorioTest {

    @Autowired
    private Repositorio repositorio;

    @Test
    @DisplayName("Criando uma Conta e salva uma conta")
    void salvar_conta_quando_sucesso(){
        Conta conta = criarConta();
        Conta contaSalva = this.repositorio.save(conta);

        Assertions.assertThat(contaSalva).isNotNull();
        Assertions.assertThat(contaSalva.getId()).isNotNull();
        Assertions.assertThat(contaSalva.getNome()).isEqualTo(conta.getNome());
    }

    @Test
    @DisplayName("Atualizando uma Conta e salva uma conta")
    void atualizar_conta_quando_sucesso(){
        Conta conta = criarConta();
        Conta contaSalva = this.repositorio.save(conta);

        contaSalva.setNome("Orlando");
        Conta contaF5 = this.repositorio.save(contaSalva);

        Assertions.assertThat(contaF5).isNotNull();
        Assertions.assertThat(contaF5.getId()).isNotNull();
        Assertions.assertThat(contaF5.getNome()).isEqualTo(contaSalva.getNome());
    }

    @Test
    @DisplayName("Delete uma Conta e salva uma conta")
    void Delete_conta_quando_sucesso(){
        Conta conta = criarConta();
        Conta contaSalva = this.repositorio.save(conta);

        this.repositorio.delete(contaSalva);

        Optional<Conta> contaOptional = this.repositorio.findById(contaSalva.getId());

        Assertions.assertThat(contaOptional).isEmpty();
    }

    private Conta criarConta(){
        return new Conta("Alana", 500, LocalDate.of(2022, 1, 22));
    }

}