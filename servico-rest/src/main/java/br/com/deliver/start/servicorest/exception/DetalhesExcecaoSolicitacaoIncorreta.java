package br.com.deliver.start.servicorest.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DetalhesExcecaoSolicitacaoIncorreta {
    private String titulo;
    private int status;
    private String detalhes;
    private String mensagemDev;
    private LocalDateTime timestemp;
}
