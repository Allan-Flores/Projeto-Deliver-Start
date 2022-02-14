package br.com.deliver.start.servicorest.exception;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ConditionalOnExpression
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExcecaoSolicitacaoIncorreta extends RuntimeException{
    public ExcecaoSolicitacaoIncorreta(String mensagem) {
        super(mensagem);
    }
}
