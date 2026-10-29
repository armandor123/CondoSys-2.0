package br.com.condosys.api.mapper;

import br.com.condosys.api.domain.model.Reserva;
import br.com.condosys.api.dto.ReservaResponse;

import org.springframework.stereotype.Component;

// Converte Reserva em DTO de resposta.
@Component
public class ReservaMapper {

    public ReservaResponse toResponse(
            Reserva reserva
    ) {

        return new ReservaResponse(
                reserva.getId(),

                reserva.getUnidade().getId(),
                reserva.getUnidade().getBloco(),
                reserva.getUnidade().getNumero(),

                reserva.getAreaComum().getId(),
                reserva.getAreaComum().getNome(),

                reserva.getCriadoPorUsuario().getId(),
                reserva.getCriadoPorUsuario()
                        .getPessoa()
                        .getNome(),

                reserva.getInicio(),
                reserva.getFim(),

                reserva.getStatus(),

                reserva.getObservacao(),
                reserva.getCriadoEm()
        );
    }
}