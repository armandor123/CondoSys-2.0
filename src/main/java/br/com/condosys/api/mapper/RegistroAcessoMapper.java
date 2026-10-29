package br.com.condosys.api.mapper;

import br.com.condosys.api.domain.model.RegistroAcesso;
import br.com.condosys.api.dto.RegistroAcessoResponse;

import org.springframework.stereotype.Component;

// Converte RegistroAcesso em DTO de resposta.
@Component
public class RegistroAcessoMapper {

    public RegistroAcessoResponse toResponse(
            RegistroAcesso registro
    ) {

        return new RegistroAcessoResponse(
                registro.getId(),

                registro.getPessoa().getId(),
                registro.getPessoa().getNome(),
                registro.getPessoa().getDocumento(),

                registro.getUnidade().getId(),
                registro.getUnidade().getBloco(),
                registro.getUnidade().getNumero(),

                registro.getPorteiro().getUsuarioId(),
                registro.getPorteiro()
                        .getUsuario()
                        .getPessoa()
                        .getNome(),

                registro.getAutorizacao() != null
                        ? registro.getAutorizacao().getId()
                        : null,

                registro.getTipoMovimento(),
                registro.getStatus(),

                registro.isLiberacaoManual(),
                registro.getMotivoLiberacao(),
                registro.getObservacao(),

                registro.getDataHora()
        );
    }
}