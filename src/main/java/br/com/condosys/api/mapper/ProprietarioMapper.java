package br.com.condosys.api.mapper;

import br.com.condosys.api.domain.model.Proprietario;
import br.com.condosys.api.dto.ProprietarioResponse;
import org.springframework.stereotype.Component;

// Converte Proprietario em DTO de resposta.
@Component
public class ProprietarioMapper {

    public ProprietarioResponse toResponse(
            Proprietario proprietario
    ) {

        return new ProprietarioResponse(
                proprietario.getUsuarioId(),
                proprietario.getUsuario().getPessoa().getNome(),
                proprietario.getUsuario().getEmail(),
                proprietario.getDataAdesao()
        );
    }
}