package br.com.condosys.api.mapper;

import br.com.condosys.api.domain.model.Encomenda;
import br.com.condosys.api.dto.EncomendaResponse;

import org.springframework.stereotype.Component;

// Converte Encomenda em DTO de resposta.
@Component
public class EncomendaMapper {

    public EncomendaResponse toResponse(
            Encomenda encomenda
    ) {

        return new EncomendaResponse(
                encomenda.getId(),

                encomenda.getUnidade().getId(),
                encomenda.getUnidade().getBloco(),
                encomenda.getUnidade().getNumero(),

                encomenda.getPorteiro().getUsuarioId(),
                encomenda.getPorteiro()
                        .getUsuario()
                        .getPessoa()
                        .getNome(),

                encomenda.getRemetente(),
                encomenda.getDestinatario(),
                encomenda.getCodigoRastreio(),
                encomenda.getDescricao(),

                encomenda.getRecebidoEm(),
                encomenda.getRetiradoEm(),

                encomenda.getStatus(),
                encomenda.getRetiradoPor()
        );
    }
}