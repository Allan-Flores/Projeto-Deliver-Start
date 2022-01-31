//DTO
package br.com.deliver.start.servicorest.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ContaReduzida {
    private int id;
    @Schema(description = "Esse é o nome do titular da conta")
    private String nome;
    @Schema(description = "Valor original do boleto")
    private double valorOriginal;
    @Schema(description = "Data de vencimento do boleto")
    private LocalDate dVencimento;

}