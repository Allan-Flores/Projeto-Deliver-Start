package br.com.deliver.start.servicorest.integration;

import br.com.deliver.start.servicorest.config.PageableResponse;
import br.com.deliver.start.servicorest.entity.Conta;
import br.com.deliver.start.servicorest.repository.Repositorio;
import br.com.deliver.start.servicorest.util.CriadorConta;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TesteIntegracao {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private Repositorio repositorio;

    @Test
    @DisplayName("Metodo Print; Retorna uma lista de conta em uma pagina")
    void print_SucessoRetornoPaginaComListaConta() {
        Conta contaSalva = repositorio.save(CriadorConta.criarContaComId());
        String nomeEsperado = contaSalva.getNome();

        PageableResponse<Conta> contaPage= testRestTemplate.exchange(
                "/print",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<Conta>>() {})
                .getBody();

        Assertions.assertThat(contaPage).isNotNull();
        Assertions.assertThat(contaPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(contaPage.toList().get(0).getNome()).isEqualTo(nomeEsperado);
    }

    @Test
    @DisplayName("Metodo Print; Retorna uma lista de conta")
    void print_SucessoRetornoListaConta() {
        Conta contaSalva = repositorio.save(CriadorConta.criarContaComId());
        String nomeEsperado = contaSalva.getNome();

        List<Conta> contaList= testRestTemplate.exchange(
                "/printNotPageable",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Conta>>() {})
                .getBody();

        Assertions.assertThat(contaList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(contaList.get(0).getNome()).isEqualTo(nomeEsperado);
    }

    @Test
    @DisplayName("Metodo procurarUmaConta; Retorna uma conta")
    void procurarUmaConta_SucessoRetornoUmaConta() {
        Conta contaSalva = repositorio.save(CriadorConta.criarContaComId());
        Conta conta = testRestTemplate.getForObject("/{id}", Conta.class, contaSalva.getId());

        Assertions.assertThat(conta).isNotNull();
        Assertions.assertThat(conta).isNotNull();
        Assertions.assertThat(conta.getNome()).isEqualTo(contaSalva.getNome());
        Assertions.assertThat(conta.getId()).isEqualTo(contaSalva.getId());
    }

    @Test
    @DisplayName("MSalvarConta; Retorna uma conta")
    void salvarConta_SucessoRetornoUmaConta() {
        Conta contasalva = CriadorConta.criarContaComId();
        ResponseEntity<Conta> rEConta = testRestTemplate.postForEntity("/", contasalva, Conta.class);

        Assertions.assertThat(rEConta).isNotNull();
        Assertions.assertThat(rEConta.getBody()).isNotNull();


        Assertions.assertThat(rEConta.getBody()).isNotNull();
        Assertions.assertThat(rEConta.getBody().getNome()).isEqualTo(contasalva.getNome());
        Assertions.assertThat(rEConta.getBody().getId()).isEqualTo(contasalva.getId());
    }

    @Test
    @DisplayName("MReplace; Atualiza uma conta")
    void replace_SucessoAtualizaConta() {
        Conta contaSalva = repositorio.save(CriadorConta.criarContaComId());
        contaSalva.setNome("Vando");

        ResponseEntity<Conta> rEConta = testRestTemplate.exchange(
                "/",
                HttpMethod.PUT,
                new HttpEntity<>(contaSalva),
                Conta.class);
        Assertions.assertThat(rEConta).isNotNull();
        Assertions.assertThat(rEConta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("MDeleta; deleta uma conta")
    void deleta_SucDeletaConta_RetContaDeletada() {
        Conta contaSalva = repositorio.save(CriadorConta.criarContaComId());

        ResponseEntity<Void> rEConta = testRestTemplate.exchange(
                "/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                contaSalva.getId());
        Assertions.assertThat(rEConta).isNotNull();
        Assertions.assertThat(rEConta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("MCalculoJuros; Atualiza VCorrigido")
    void calculoJuros_SucessoAtualizaVCorrigido() {
        Conta contaSalva = repositorio.save(CriadorConta.criarContaComId());

        PageableResponse<Conta> contaPage = testRestTemplate.exchange(
                        "/juros",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<PageableResponse<Conta>>() {
                        })
                .getBody();

        Assertions.assertThat(contaPage).isNotNull();

        Conta conta = contaPage.toList().get(0);
        Assertions.assertThat(conta).isNotNull();
        Assertions.assertThat(conta.getAtraso()).isGreaterThan(0);
        Assertions.assertThat(conta.getValorCorrigido()).isGreaterThan(0.0);
    }

    @Test
    @DisplayName("MErro; Retorna uma mensagem de erro")
    void erro_SucessoRetornoMsg() {

        String msg = testRestTemplate.exchange(
                        "/erro",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<String>() {})
                .getBody();

        Assertions.assertThat(msg)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo("Algo deu errado");
    }

    @Test
    @DisplayName("MPrograma; Retorna pagina de contas - rotina do sistema")
    void procurarUmaConta_SucExecutaRotinaSistema_RetPaginaConta() {
        PageableResponse<Conta> contaPage= testRestTemplate.exchange(
                        "/",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<PageableResponse<Conta>>() {})
                .getBody();

        Assertions.assertThat(contaPage).isNotNull();
        Assertions.assertThat(contaPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(contaPage.toList().get(0).getId()).isEqualTo(1);
    }
}
