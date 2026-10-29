package br.com.condosys.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

// Dados recebidos para registrar uma encomenda.
public record EncomendaRequest(

        // Unidade destinatária.
        @NotNull(message = "A unidade é obrigatória.")
        UUID unidadeId,

        // Porteiro que recebeu a encomenda.
        @NotNull(message = "O porteiro é obrigatório.")
        UUID porteiroId,

        // Empresa ou pessoa remetente.
        @Size(
                max = 150,
                message = "O remetente deve ter no máximo 150 caracteres."
        )
        String remetente,

        // Pessoa destinatária.
        @NotBlank(message = "O destinatário é obrigatório.")
        @Size(
                max = 150,
                message = "O destinatário deve ter no máximo 150 caracteres."
        )
        String destinatario,

        // Código de rastreamento.
        @Size(
                max = 100,
                message = "O código de rastreio deve ter no máximo 100 caracteres."
        )
        String codigoRastreio,

        // Informação adicional.
        String descricao

) {
}