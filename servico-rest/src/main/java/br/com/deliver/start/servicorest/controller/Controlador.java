package br.com.deliver.start.servicorest.controller;

import br.com.deliver.start.servicorest.entity.Conta;
import br.com.deliver.start.servicorest.entity.ContaReduzida;
import br.com.deliver.start.servicorest.service.ServicoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/")
public class Controlador {

    @Autowired
    private ServicoUsuario servicoUser;

    @RequestMapping//GetMapping
    public ResponseEntity<Page<Conta>> programa(Pageable pageable) {
        servicoUser.novaConta();
        servicoUser.calculoJuros();
        return new ResponseEntity<>(servicoUser.listaTodosP(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}") //t
    public ResponseEntity<Conta> procurarUmaConta(@PathVariable int id) {
        return new ResponseEntity<>(servicoUser.consultarConta(id), HttpStatus.OK);
    }

    @PostMapping //t
    public ResponseEntity<Conta> salvarConta(@RequestBody ContaReduzida conta) {
        return new ResponseEntity<>(servicoUser.salvarConta(conta), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}") //t
    public ResponseEntity<Void> deleta(@PathVariable int id) {
        servicoUser.deleta(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping //t
    public ResponseEntity<Void> replace(@RequestBody ContaReduzida conta) {
        servicoUser.replace(conta);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping("/juros") //t
    public ResponseEntity<Page<Conta>> juros(Pageable pageable) {
        servicoUser.calculoJuros();
        return new ResponseEntity<>(servicoUser.listaTodosP(pageable), HttpStatus.OK);
    }

    @RequestMapping("/print") //t
    public ResponseEntity<Page<Conta>> print(Pageable pageable) {
        return new ResponseEntity<>(servicoUser.listaTodosP(pageable), HttpStatus.OK);
    }

    @RequestMapping("/printNotPageable") //t
    public ResponseEntity<List<Conta>> print() {
        return new ResponseEntity<>(servicoUser.listaTodosL(), HttpStatus.OK);
    }

    @RequestMapping("/erro") //t
    public String erro() {
        return "Algo deu errado";
    }
}