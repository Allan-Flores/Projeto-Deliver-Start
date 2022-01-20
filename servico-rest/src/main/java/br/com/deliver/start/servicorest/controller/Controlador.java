package br.com.deliver.start.servicorest.controller;

import br.com.deliver.start.servicorest.entity.Conta;
import br.com.deliver.start.servicorest.service.ServicoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/")
public class Controlador {

    @Autowired
    private ServicoUsuario servicoUser;

    @RequestMapping
    public ResponseEntity<Page<Conta>> programa(Pageable pageable) {
        servicoUser.criarContas();
        servicoUser.novaConta();
        servicoUser.calculoJuros();
        return new ResponseEntity<>(servicoUser.listaTodos(pageable), HttpStatus.OK);
    }

    @RequestMapping(path = "/{id}")
    public ResponseEntity<Conta> procurarUmaConta(@PathVariable int id) {
        return new ResponseEntity<>(servicoUser.consultarConta(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Conta> salvarConta(@RequestBody @Valid Conta conta) {
        return new ResponseEntity<>(servicoUser.salvarConta(conta), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleta(@PathVariable int id) {
        servicoUser.deleta(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody Conta conta) {
        servicoUser.replace(conta);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping("/rest")
    public String programa2() {
        servicoUser.criarContas();
        String mensagem = "Contas Criadas";
        servicoUser.novaConta();
        mensagem = "\nnova conta criada";
        servicoUser.calculoJuros();
        return "Tudo feito com sucesso";
    }

    @RequestMapping("/print")
    public ResponseEntity<Page<Conta>> print(Pageable pageable) {
        return new ResponseEntity<>(servicoUser.listaTodos(pageable), HttpStatus.OK);
    }

    @RequestMapping("/erro")
    public String erro() {
        return "Algo deu errado";
    }


}