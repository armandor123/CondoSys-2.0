package br.com.condosys.api.dto;

import br.com.condosys.api.domain.enums.StatusAcesso;
import br.com.condosys.api.domain.enums.TipoMovimentoAcesso;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

// Dados recebidos para registrar uma entrada ou saída.
public record RegistroAcessoRequest(

        // Pessoa relacionada ao acesso.
        @NotNull(message = "A pessoa é obrigatória.")
        UUID pessoaId,

        // Unidade de destino.
        @NotNull(message = "A unidade é obrigatória.")
        UUID unidadeId,

        // Porteiro responsável.
        @NotNull(message = "O porteiro é obrigatório.")
        UUID porteiroId,

        // Pode ser nula em acesso negado ou liberação manual.
        UUID autorizacaoId,

        // Entrada ou saída.
        @NotNull(message = "O tipo de movimento é obrigatório.")
        TipoMovimentoAcesso tipoMovimento,

        // Autorizado ou negado.
        @NotNull(message = "O status é obrigatório.")
        StatusAcesso status,

        // Indica liberação realizada manualmente.
        boolean liberacaoManual,

        // Obrigatório quando a liberação for manual.
        String motivoLiberacao,

        // Informação adicional.
        String observacao

) {
}