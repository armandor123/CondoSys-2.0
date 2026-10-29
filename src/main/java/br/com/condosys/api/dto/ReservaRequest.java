package br.com.condosys.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

// Dados recebidos para criar uma reserva.
public record ReservaRequest(

        @NotNull(message = "A unidade é obrigatória.")
        UUID unidadeId,

        @NotNull(message = "A área comum é obrigatória.")
        UUID areaComumId,

        @NotNull(message = "O usuário responsável é obrigatório.")
        UUID usuarioId,

        @NotNull(message = "O início da reserva é obrigatório.")
        OffsetDateTime inicio,

        @NotNull(message = "O fim da reserva é obrigatório.")
        OffsetDateTime fim,

        String observacao

) {
}