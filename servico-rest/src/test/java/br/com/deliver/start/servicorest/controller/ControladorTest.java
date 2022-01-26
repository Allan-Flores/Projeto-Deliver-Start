package br.com.deliver.start.servicorest.controller;

import br.com.deliver.start.servicorest.entity.Conta;
import br.com.deliver.start.servicorest.service.ServicoUsuario;
import br.com.deliver.start.servicorest.util.CriadorConta;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private ServicoUsuario servicoUsuarioMock;

    @BeforeEach
    void setUp() {
        PageImpl<Conta> contaPage = new PageImpl<>(List.of(CriadorConta.criarContaComId()));

        BDDMockito.when(servicoUsuarioMock.listaTodosP(ArgumentMatchers.any())).thenReturn(contaPage);
        BDDMockito.when(servicoUsuarioMock.listaTodosL()).thenReturn(List.of(CriadorConta.criarContaComId()));
        BDDMockito.when(servicoUsuarioMock.consultarConta(ArgumentMatchers.anyInt())).thenReturn(CriadorConta.criarContaComId());
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
}