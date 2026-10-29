package br.com.condosys.api.mapper;

import br.com.condosys.api.domain.model.AreaComum;
import br.com.condosys.api.dto.AreaComumResponse;

import org.springframework.stereotype.Component;

// Converte AreaComum em DTO de resposta.
@Component
public class AreaComumMapper {

    public AreaComumResponse toResponse(
            AreaComum areaComum
    ) {

        return new AreaComumResponse(
                areaComum.getId(),
                areaComum.getNome(),
                areaComum.getCapacidade(),
                areaComum.isAtiva(),
                areaComum.getDescricao()
        );
    }
}