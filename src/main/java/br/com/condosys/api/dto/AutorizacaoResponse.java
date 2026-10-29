package br.com.condosys.api.dto;

import br.com.condosys.api.domain.enums.StatusAutorizacao;
import br.com.condosys.api.domain.enums.TipoAutorizacao;

import java.time.OffsetDateTime;
import java.util.UUID;

// Dados devolvidos pela API sobre uma autorização.
public record AutorizacaoResponse(

        UUID id,

        UUID pessoaId,
        String pessoaNome,
        String pessoaDocumento,

        UUID proprietarioId,
        String proprietarioNome,

        UUID unidadeId,
        String bloco,
        String numero,

        TipoAutorizacao tipo,
        StatusAutorizacao status,

        OffsetDateTime inicioValidade,
        OffsetDateTime fimValidade,

        String observacao,

        OffsetDateTime criadoEm,
        OffsetDateTime revogadoEm

) {
}