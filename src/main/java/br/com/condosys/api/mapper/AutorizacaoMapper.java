package br.com.condosys.api.mapper;

import br.com.condosys.api.domain.model.Autorizacao;
import br.com.condosys.api.dto.AutorizacaoResponse;

import org.springframework.stereotype.Component;

// Converte Autorizacao em DTO de resposta.
@Component
public class AutorizacaoMapper {

    public AutorizacaoResponse toResponse(
            Autorizacao autorizacao
    ) {

        return new AutorizacaoResponse(
                autorizacao.getId(),

                autorizacao.getPessoa().getId(),
                autorizacao.getPessoa().getNome(),
                autorizacao.getPessoa().getDocumento(),

                autorizacao.getProprietario().getUsuarioId(),
                autorizacao.getProprietario()
                        .getUsuario()
                        .getPessoa()
                        .getNome(),

                autorizacao.getUnidade().getId(),
                autorizacao.getUnidade().getBloco(),
                autorizacao.getUnidade().getNumero(),

                autorizacao.getTipo(),
                autorizacao.getStatus(),

                autorizacao.getInicioValidade(),
                autorizacao.getFimValidade(),

                autorizacao.getObservacao(),

                autorizacao.getCriadoEm(),
                autorizacao.getRevogadoEm()
        );
    }
}