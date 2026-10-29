package br.com.condosys.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

// Dados recebidos para cadastrar um visitante.
public record VisitanteRequest(

        // Pessoa previamente cadastrada como VISITANTE.
        @NotNull(message = "A pessoa é obrigatória.")
        UUID pessoaId,

        // Informação opcional sobre o visitante.
        String observacao

) {
}