package br.com.deliver.start.servicorest.controller;

import br.com.deliver.start.servicorest.entity.Conta;
import br.com.deliver.start.servicorest.entity.ContaReduzida;
import br.com.deliver.start.servicorest.service.ServicoUsuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class Controlador {

    @Autowired
    private ServicoUsuario servicoUser;

    @GetMapping(path = "/rotina")
    @Operation(summary = "Executa rotina do sistema", description = "Rotina: cria-se uma nova conta e executa a rotina /juros")
    public ResponseEntity<Page<Conta>> programa(@ParameterObject Pageable pageable) {
        servicoUser.novaConta();
        servicoUser.calculoJuros();
        return new ResponseEntity<>(servicoUser.listaTodosP(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/procuraconta/{id}")
    @Operation(summary = "Procura uma conta", description = "O sistema fará a procura da conta no banco de dados a partir do id informado")
    public ResponseEntity<Conta> procurarUmaConta(@PathVariable int id) {
        return new ResponseEntity<>(servicoUser.consultarConta(id), HttpStatus.OK);
    }

    @PostMapping(path = "/salvaconta")
    @Operation(summary = "Salva a nova conta informada")
    public ResponseEntity<Conta> salvarConta(@RequestBody ContaReduzida conta) {
        return new ResponseEntity<>(servicoUser.salvarConta(conta), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/deleta/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sucesso na operação"),
            @ApiResponse(responseCode = "200", description = "Conta não existe no banco de dados")
    })
    @Operation(summary = "Deleta uma conta existente", description = "A partir do id informado o sistema deletará a conta caso exista")
    public ResponseEntity<Void> deleta(@PathVariable int id) {
        servicoUser.deleta(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/replace")
    @Operation(summary = "Atualiza cadastro", description = "Insere no banco de dados as novas informações informadas na conta especificada")
    public ResponseEntity<Void> replace(@RequestBody ContaReduzida conta) {
        servicoUser.replace(conta);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/juros")
    @Operation(summary = "Executa o cálculo de juros", description = "Lança rotina que atualiza todas as contas no banco de dados com os dias de atraso atual e o valor do boleto com juros corrigidos")
    public ResponseEntity<Page<Conta>> juros(@ParameterObject Pageable pageable) {
        servicoUser.calculoJuros();
        return new ResponseEntity<>(servicoUser.listaTodosP(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/print")
    @Operation(summary = "Lista todas as contas em paginas")
    public ResponseEntity<Page<Conta>> print(@ParameterObject Pageable pageable) {
        return new ResponseEntity<>(servicoUser.listaTodosP(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/printNotPageable")
    @Operation(summary = "Lista todas as contas")
    public ResponseEntity<List<Conta>> print() {
        return new ResponseEntity<>(servicoUser.listaTodosL(), HttpStatus.OK);
    }

    @GetMapping(path = "/erro")
    public String erro() {
        return "Algo deu errado";
    }
}