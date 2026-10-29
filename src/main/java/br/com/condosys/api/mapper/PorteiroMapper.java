package br.com.condosys.api.mapper;

import br.com.condosys.api.domain.model.Porteiro;
import br.com.condosys.api.dto.PorteiroResponse;
import org.springframework.stereotype.Component;

// Converte Porteiro em DTO de resposta.
@Component
public class PorteiroMapper {

    public PorteiroResponse toResponse(
            Porteiro porteiro
    ) {

        return new PorteiroResponse(
                porteiro.getUsuarioId(),
                porteiro.getUsuario().getPessoa().getNome(),
                porteiro.getUsuario().getEmail(),
                porteiro.getTurno(),
                porteiro.getDataDemissao()
        );
    }
}