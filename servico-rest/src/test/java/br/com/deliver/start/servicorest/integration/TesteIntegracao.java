package br.com.deliver.start.servicorest.integration;

import br.com.deliver.start.servicorest.config.PageableResponse;
import br.com.deliver.start.servicorest.entity.Conta;
import br.com.deliver.start.servicorest.entity.UsuarioSistema;
import br.com.deliver.start.servicorest.repository.Repositorio;
import br.com.deliver.start.servicorest.repository.RepositorioUsuarioSistema;
import br.com.deliver.start.servicorest.util.CriadorConta;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
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
    @Qualifier("testRestTemplateRegrasUsuario")
    private TestRestTemplate testRestTemplateRegraUsuario;
    @Autowired
    @Qualifier("testRestTemplateRegrasAdmin")
    private TestRestTemplate testRestTemplateRegraAdmin;
    @Autowired
    private Repositorio repositorio;
    @Autowired
    private RepositorioUsuarioSistema repositorioUsuarioSistema;

    private static final UsuarioSistema USER = UsuarioSistema.builder()
            .name("flores teste")
            .password("{bcrypt}$2a$10$EEf.z7gtYaF6XppOHLI8Pu7K5PWZTP4E/0aJN4zvOA52aY1Lp9WPO")
            .username("floresteste")
            .authorities("ROLE_USER")
            .build();
    private static final UsuarioSistema ADMIN = UsuarioSistema.builder()
            .name("allan teste")
            .password("{bcrypt}$2a$10$EEf.z7gtYaF6XppOHLI8Pu7K5PWZTP4E/0aJN4zvOA52aY1Lp9WPO")
            .username("allanteste")
            .authorities("ROLE_USER,ROLE_ADMIN")
            .build();

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRegrasUsuario")
        public TestRestTemplate testRestTemplateRegrasCriadorUsuario(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("floresteste", "test");
            return new TestRestTemplate(restTemplateBuilder);
        }
        @Bean(name = "testRestTemplateRegrasAdmin")
        public TestRestTemplate testRestTemplateRegrasCriadorAdmin(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("allanteste", "test");
            return new TestRestTemplate(restTemplateBuilder);
        }
    }
    @Test
    @DisplayName("Metodo Print; Retorna uma lista de conta em uma pagina")
    void print_SucessoRetornoPaginaComListaConta() {
        Conta contaSalva = repositorio.save(CriadorConta.criarContaComId());
        repositorioUsuarioSistema.save(USER);

        String nomeEsperado = contaSalva.getNome();

        PageableResponse<Conta> contaPage= testRestTemplateRegraUsuario.exchange(
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
        repositorioUsuarioSistema.save(USER);

        List<Conta> contaList= testRestTemplateRegraUsuario.exchange(
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
        repositorioUsuarioSistema.save(USER);

        Conta conta = testRestTemplateRegraUsuario.getForObject("/{id}", Conta.class, contaSalva.getId());

        Assertions.assertThat(conta).isNotNull();
        Assertions.assertThat(conta.getNome()).isEqualTo(contaSalva.getNome());
        Assertions.assertThat(conta.getId()).isEqualTo(contaSalva.getId());
    }

    @Test
    @DisplayName("MSalvarConta; Retorna uma conta")
    void salvarConta_SucessoRetornoUmaConta() {
        Conta contaCriada = CriadorConta.criarContaComId();

        repositorioUsuarioSistema.save(ADMIN);

        ResponseEntity<Conta> rEConta = testRestTemplateRegraAdmin.postForEntity("/", contaCriada, Conta.class);


        Assertions.assertThat(rEConta).isNotNull();
        Assertions.assertThat(rEConta.getBody()).isNotNull();
        Assertions.assertThat(rEConta.getBody().getNome()).isEqualTo(contaCriada.getNome());
        //Assertions.assertThat(rEConta.getBody().getId()).isEqualTo(contaCriada.getId());
    }

    @Test
    @DisplayName("MReplace; Atualiza uma conta")
    void replace_SucessoAtualizaConta() {
        Conta contaSalva = repositorio.save(CriadorConta.criarContaComId());
        contaSalva.setNome("Vando");
        repositorioUsuarioSistema.save(USER);

        ResponseEntity<Conta> rEConta = testRestTemplateRegraUsuario.exchange(
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
        repositorioUsuarioSistema.save(ADMIN);

        ResponseEntity<Void> rEConta = testRestTemplateRegraAdmin.exchange(
                "/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                contaSalva.getId());
        Assertions.assertThat(rEConta).isNotNull();
        Assertions.assertThat(rEConta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("MDeleta; retorna 403 quando o usuario não for admin")
    void deleta_SucExcessoNegado_RetExcecao304() {
        Conta contaSalva = repositorio.save(CriadorConta.criarContaComId());
        repositorioUsuarioSistema.save(USER);

        ResponseEntity<Void> rEConta = testRestTemplateRegraUsuario.exchange(
                "/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                contaSalva.getId());
        Assertions.assertThat(rEConta).isNotNull();
        Assertions.assertThat(rEConta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("MCalculoJuros; Atualiza VCorrigido")
    void calculoJuros_SucessoAtualizaVCorrigido() {
        Conta contaSalva = repositorio.save(CriadorConta.criarContaComId());
        repositorioUsuarioSistema.save(USER);

        PageableResponse<Conta> contaPage = testRestTemplateRegraUsuario.exchange(
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
        repositorioUsuarioSistema.save(USER);

        String msg = testRestTemplateRegraUsuario.exchange(
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
        repositorioUsuarioSistema.save(USER);
        
        PageableResponse<Conta> contaPage= testRestTemplateRegraUsuario.exchange(
                        "/",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<PageableResponse<Conta>>() {})
                .getBody();

        Assertions.assertThat(contaPage).isNotNull();
        Assertions.assertThat(contaPage.toList())
                .isNotEmpty()
                .hasSize(1);
        //Assertions.assertThat(contaPage.toList().get(0).getId()).isEqualTo(1);
    }
}
