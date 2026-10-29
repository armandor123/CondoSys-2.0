package br.com.condosys.api.dto;

import br.com.condosys.api.domain.enums.TipoAutorizacao;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

// Dados recebidos para criar uma autorização.
public record AutorizacaoRequest(

        // Pessoa autorizada.
        @NotNull(message = "A pessoa é obrigatória.")
        UUID pessoaId,

        // Proprietário responsável.
        @NotNull(message = "O proprietário é obrigatório.")
        UUID proprietarioId,

        // Unidade relacionada.
        @NotNull(message = "A unidade é obrigatória.")
        UUID unidadeId,

        // Tipo da autorização.
        @NotNull(message = "O tipo da autorização é obrigatório.")
        TipoAutorizacao tipo,

        // Início da validade.
        @NotNull(message = "O início da validade é obrigatório.")
        OffsetDateTime inicioValidade,

        // Final da validade, quando aplicável.
        OffsetDateTime fimValidade,

        // Informação adicional.
        String observacao

) {
}