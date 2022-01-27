package br.com.deliver.start.servicorest.service;

import br.com.deliver.start.servicorest.entity.Conta;
import br.com.deliver.start.servicorest.exception.ExcecaoSolicitacaoIncorreta;
import br.com.deliver.start.servicorest.repository.Repositorio;
import br.com.deliver.start.servicorest.util.CriadorConta;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class ServicoUsuarioTest {
    //Uso na classe testada
    @InjectMocks
    private ServicoUsuario servicoUsuario;

    //Uso nas clesses que auxilian o funcionamnto da classe testada
    @Mock
    private Repositorio repositorioMock;

    @BeforeEach
    void setUp() {
        PageImpl<Conta> contaPage = new PageImpl<>(List.of(CriadorConta.criarContaComId()));
        BDDMockito.when(repositorioMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(contaPage);

        BDDMockito.when(repositorioMock.findAll())
                .thenReturn(List.of(CriadorConta.criarContaComId()));

        BDDMockito.when(repositorioMock.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Optional.of(CriadorConta.criarContaComId()));

        BDDMockito.when(repositorioMock.save(ArgumentMatchers.any(Conta.class)))
                .thenReturn(CriadorConta.criarContaComId());

        //BDDMockito.doNothing().when(repositorioMock).save(ArgumentMatchers.any(Conta.class));

        BDDMockito.doNothing().when(repositorioMock).delete(ArgumentMatchers.any(Conta.class));

        List<Conta> contaList = new ArrayList<>(List.of(CriadorConta.criarContaComId()));
        //BDDMockito.when(repositorioMock.saveAll(contaList)).thenReturn(contaList);
        BDDMockito.when(repositorioMock.saveAll(ArgumentMatchers.anyList())).thenReturn(contaList);
    }

    @Test
    @DisplayName("MListaTodosP; Retorna uma lista de conta em uma pagina")
    void listaTodosP_SucessoRetornoPaginaComListaConta() {
        String nomeEsperado = CriadorConta.criarContaComId().getNome();
        Page<Conta> contaPage = servicoUsuario.listaTodosP(PageRequest.of(1,1));

        Assertions.assertThat(contaPage).isNotNull();
        Assertions.assertThat(contaPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(contaPage.toList().get(0).getNome()).isEqualTo(nomeEsperado);
    }

    @Test
    @DisplayName("MListaTodosL; Retorna uma lista de conta")
    void listaTodosL_SucessoRetornoListaConta() {
        String nomeEsperado = CriadorConta.criarContaComId().getNome();
        List<Conta> contaList = servicoUsuario.listaTodosL();

        Assertions.assertThat(contaList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(contaList.get(0).getNome()).isEqualTo(nomeEsperado);
    }

    @Test
    @DisplayName("MConsultarConta; Retorna uma conta")
    void consultarConta_SucessoRetornoUmaConta() {
        Conta contaEsperada = CriadorConta.criarContaComId();
        Conta contaConsultar = servicoUsuario.consultarConta(contaEsperada.getId());

        Assertions.assertThat(contaConsultar).isNotNull();
        Assertions.assertThat(contaConsultar.getNome()).isEqualTo(contaEsperada.getNome());
        Assertions.assertThat(contaConsultar.getId()).isEqualTo(contaEsperada.getId());
    }

    @Test
    @DisplayName("MConsultarConta; Retorno quando a conta não existe")
    void consultarConta_SucessoRetornoExcecõ() {
        BDDMockito.when(repositorioMock.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ExcecaoSolicitacaoIncorreta.class)
                .isThrownBy( () -> servicoUsuario.consultarConta(1));
    }

    @Test
    @DisplayName("MSalvarConta; Retorna uma conta")
    void salvarConta_SucessoRetornoUmaConta() {
        Conta contaEsperada = CriadorConta.criarContaComId();
        Conta salvarConta = servicoUsuario.salvarConta(CriadorConta.criarContaReduz());

        Assertions.assertThat(salvarConta).isNotNull();
        Assertions.assertThat(salvarConta.getNome()).isEqualTo(contaEsperada.getNome());
        Assertions.assertThat(salvarConta.getId()).isEqualTo(contaEsperada.getId());
    }

    @Test
    @DisplayName("MReplace; Atualiza uma conta")
    void replace_SucessoAtualizaConta() {
        Assertions.assertThatCode(() -> servicoUsuario.replace(CriadorConta.criarContaReduz()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("MDeleta; deleta uma conta")
    void deleta_SucessoDeletaConta() {
        Assertions.assertThatCode(() -> servicoUsuario.deleta(1))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("MCalculoJuros; Atualiza VCorrigido")
    void calculoJuros_SucessoAtualizaVCorrigido() {
        Assertions.assertThatCode(() -> servicoUsuario.calculoJuros())
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("MNovaConta; Retorna uma conta")
    void novaConta_SucessoRetornoUmaConta() {
        Conta contaEsperada = CriadorConta.criarContaComId();
        servicoUsuario.novaConta();
    }
}