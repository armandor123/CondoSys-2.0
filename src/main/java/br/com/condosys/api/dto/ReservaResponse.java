package br.com.condosys.api.dto;

import br.com.condosys.api.domain.enums.StatusReserva;

import java.time.OffsetDateTime;
import java.util.UUID;

// Dados devolvidos pela API sobre uma reserva.
public record ReservaResponse(

        UUID id,

        UUID unidadeId,
        String bloco,
        String numero,

        UUID areaComumId,
        String areaComumNome,

        UUID usuarioId,
        String usuarioNome,

        OffsetDateTime inicio,
        OffsetDateTime fim,

        StatusReserva status,

        String observacao,
        OffsetDateTime criadoEm

) {
}