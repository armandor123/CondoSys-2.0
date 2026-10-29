package br.com.condosys.api.mapper;

import br.com.condosys.api.domain.model.Visitante;
import br.com.condosys.api.dto.VisitanteResponse;

import org.springframework.stereotype.Component;

// Converte Visitante em DTO de resposta.
@Component
public class VisitanteMapper {

    public VisitanteResponse toResponse(
            Visitante visitante
    ) {

        return new VisitanteResponse(
                visitante.getPessoaId(),
                visitante.getPessoa().getNome(),
                visitante.getPessoa().getDocumento(),
                visitante.getPessoa().getTelefone(),
                visitante.getObservacao()
        );
    }
}