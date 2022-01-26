//DTO
package br.com.deliver.start.servicorest.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

import static br.com.deliver.start.servicorest.config.Uteis.formatter;

@Data
@Builder
public class ContaReduzida {
    private int id;
    private String nome;
    private double valorOriginal;
    private LocalDate dVencimento;

}