package br.com.condosys.api.dto;

import br.com.condosys.api.domain.enums.StatusEncomenda;

import java.time.OffsetDateTime;
import java.util.UUID;

// Dados devolvidos pela API sobre uma encomenda.
public record EncomendaResponse(

        UUID id,

        UUID unidadeId,
        String bloco,
        String numero,

        UUID porteiroId,
        String porteiroNome,

        String remetente,
        String destinatario,
        String codigoRastreio,
        String descricao,

        OffsetDateTime recebidoEm,
        OffsetDateTime retiradoEm,

        StatusEncomenda status,
        String retiradoPor

) {
}