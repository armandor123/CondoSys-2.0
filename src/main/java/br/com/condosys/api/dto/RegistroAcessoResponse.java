package br.com.condosys.api.dto;

import br.com.condosys.api.domain.enums.StatusAcesso;
import br.com.condosys.api.domain.enums.TipoMovimentoAcesso;

import java.time.OffsetDateTime;
import java.util.UUID;

// Dados devolvidos pela API sobre um registro de acesso.
public record RegistroAcessoResponse(

        UUID id,

        UUID pessoaId,
        String pessoaNome,
        String pessoaDocumento,

        UUID unidadeId,
        String bloco,
        String numero,

        UUID porteiroId,
        String porteiroNome,

        UUID autorizacaoId,

        TipoMovimentoAcesso tipoMovimento,
        StatusAcesso status,

        boolean liberacaoManual,
        String motivoLiberacao,
        String observacao,

        OffsetDateTime dataHora

) {
}