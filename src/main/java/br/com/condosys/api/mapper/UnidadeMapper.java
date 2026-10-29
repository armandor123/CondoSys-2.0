package br.com.condosys.api.mapper;

import br.com.condosys.api.domain.model.Unidade;
import br.com.condosys.api.dto.UnidadeResponse;
import org.springframework.stereotype.Component;

// Converte Unidade em DTO de resposta.
@Component
public class UnidadeMapper {

    public UnidadeResponse toResponse(
            Unidade unidade
    ) {

        return new UnidadeResponse(
                unidade.getId(),
                unidade.getProprietario().getUsuarioId(),
                unidade.getProprietario().getUsuario().getPessoa().getNome(),
                unidade.getBloco(),
                unidade.getNumero(),
                unidade.getAndar(),
                unidade.isAtiva()
        );
    }
}