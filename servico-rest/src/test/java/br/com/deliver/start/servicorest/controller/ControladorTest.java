package br.com.deliver.start.servicorest.controller;

import br.com.deliver.start.servicorest.entity.Conta;
import br.com.deliver.start.servicorest.entity.ContaReduzida;
import br.com.deliver.start.servicorest.service.ServicoConta;
import br.com.deliver.start.servicorest.util.CriadorConta;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
class ControladorTest {
    //Uso na classe testada
    @InjectMocks
    private Controlador controlador;

    //Uso nas clesses que auxilian o funcionamnto da classe testada
    @Mock
    private ServicoConta servicoContaMock;

    @BeforeEach
    void setUp() {
        PageImpl<Conta> contaPage = new PageImpl<>(List.of(CriadorConta.criarContaComId()));

        BDDMockito.when(servicoContaMock.listaTodosP(ArgumentMatchers.any()))
                .thenReturn(contaPage);

        BDDMockito.when(servicoContaMock.listaTodosL())
                .thenReturn(List.of(CriadorConta.criarContaComId()));

        BDDMockito.when(servicoContaMock.consultarConta(ArgumentMatchers.anyInt()))
                .thenReturn(CriadorConta.criarContaComId());

        BDDMockito.when(servicoContaMock.salvarConta(ArgumentMatchers.any(ContaReduzida.class)))
                .thenReturn(CriadorConta.criarContaComId());

        BDDMockito.doNothing().when(servicoContaMock).replace(ArgumentMatchers.any(ContaReduzida.class));

        BDDMockito.doNothing().when(servicoContaMock).deleta(ArgumentMatchers.anyInt());

        BDDMockito.doNothing().when(servicoContaMock).calculoJuros();
    }

    @Test
    @DisplayName("Metodo Print; Retorna uma lista de conta em uma pagina")
    void print_SucessoRetornoPaginaComListaConta() {
        String nomeEsperado = CriadorConta.criarContaComId().getNome();
        Page<Conta> contaPage = controlador.print(null).getBody();

        Assertions.assertThat(contaPage).isNotNull();
        Assertions.assertThat(contaPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(contaPage.toList().get(0).getNome()).isEqualTo(nomeEsperado);
    }

    @Test
    @DisplayName("Metodo Print; Retorna uma lista de conta")
    void print_SucessoRetornoListaConta() {
        String nomeEsperado = CriadorConta.criarContaComId().getNome();
        List<Conta> contaList = controlador.print().getBody();

        Assertions.assertThat(contaList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(contaList.get(0).getNome()).isEqualTo(nomeEsperado);
    }

    @Test
    @DisplayName("Metodo procurarUmaConta; Retorna uma conta")
    void procurarUmaConta_SucessoRetornoUmaConta() {
        Conta contaEsperada = CriadorConta.criarContaComId();
        ResponseEntity<Conta> rEConta = controlador.procurarUmaConta(contaEsperada.getId());

        Assertions.assertThat(rEConta).isNotNull();
        Assertions.assertThat(rEConta.getBody()).isNotNull();
        Assertions.assertThat(rEConta.getBody().getNome()).isEqualTo(contaEsperada.getNome());
        Assertions.assertThat(rEConta.getBody().getId()).isEqualTo(contaEsperada.getId());
    }

    @Test
    @DisplayName("MSalvarConta; Retorna uma conta")
    void salvarConta_SucessoRetornoUmaConta() {
        Conta contaEsperada = CriadorConta.criarContaComId();
        ResponseEntity<Conta> rEConta = controlador.salvarConta(CriadorConta.criarContaReduz());

        Assertions.assertThat(rEConta).isNotNull();
        Assertions.assertThat(rEConta.getBody()).isNotNull();


        Assertions.assertThat(rEConta.getBody()).isNotNull();
        Assertions.assertThat(rEConta.getBody().getNome()).isEqualTo(contaEsperada.getNome());
        Assertions.assertThat(rEConta.getBody().getId()).isEqualTo(contaEsperada.getId());
    }

    @Test
    @DisplayName("MReplace; Atualiza uma conta")
    void replace_SucessoAtualizaConta() {
        Assertions.assertThatCode(() -> controlador.replace(CriadorConta.criarContaReduz()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> rEConta = controlador.replace(CriadorConta.criarContaReduz());
        Assertions.assertThat(rEConta).isNotNull();
        Assertions.assertThat(rEConta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("MDeleta; deleta uma conta")
    void deleta_SucessoDeletaConta() {
        Assertions.assertThatCode(() -> controlador.deleta(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> rEConta = controlador.deleta(1);
        Assertions.assertThat(rEConta).isNotNull();
        Assertions.assertThat(rEConta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("MCalculoJuros; Atualiza VCorrigido")
    void calculoJuros_SucessoAtualizaVCorrigido() {
        Assertions.assertThatCode(() -> controlador.juros(null))
                .doesNotThrowAnyException();

        ResponseEntity<Page<Conta>> rEConta = controlador.juros(null);
        Assertions.assertThat(rEConta).isNotNull();
        Assertions.assertThat(rEConta.getStatusCode()).isEqualTo(HttpStatus.OK);

        Conta conta = rEConta.getBody().toList().get(0);
        Assertions.assertThat(conta).isNotNull();
    }
}