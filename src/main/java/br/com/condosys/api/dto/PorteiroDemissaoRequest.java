package br.com.condosys.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

// Dados recebidos para registrar a demissão de um porteiro.
public record PorteiroDemissaoRequest(

        // Data em que ocorreu a demissão.
        @NotNull(message = "A data de demissão é obrigatória.")
        @PastOrPresent(message = "A data de demissão não pode ser futura.")
        LocalDate dataDemissao

) {
}