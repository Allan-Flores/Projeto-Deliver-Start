package br.com.deliver.start.servicorest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ExcecaoSolicitacaoIncorreta.class)
    public ResponseEntity<DetalhesExcecaoSolicitacaoIncorreta> handlerExcecaoSolicitacaoIncorreta(ExcecaoSolicitacaoIncorreta esi){
        return new ResponseEntity<>(
                DetalhesExcecaoSolicitacaoIncorreta.builder()
                        .timestemp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .titulo("Exceção na solicitação incorreta")
                        .detalhes(esi.getMessage())
                        .mensagemDev(esi.getClass().getName())
                        .build(), HttpStatus.BAD_REQUEST);
    }
}
