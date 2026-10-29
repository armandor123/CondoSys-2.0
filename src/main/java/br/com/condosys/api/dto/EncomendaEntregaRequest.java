package br.com.condosys.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Dados utilizados para registrar a retirada.
public record EncomendaEntregaRequest(

        // Nome de quem recebeu a encomenda.
        @NotBlank(message = "Informe quem retirou a encomenda.")
        @Size(
                max = 150,
                message = "O nome deve ter no máximo 150 caracteres."
        )
        String retiradoPor

) {
}