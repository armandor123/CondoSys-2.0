package br.com.condosys.api.mapper;

import br.com.condosys.api.domain.model.Administrador;
import br.com.condosys.api.dto.AdministradorResponse;
import org.springframework.stereotype.Component;

// Converte Administrador em DTO de resposta.
@Component
public class AdministradorMapper {

    public AdministradorResponse toResponse(
            Administrador administrador
    ) {

        return new AdministradorResponse(
                administrador.getUsuarioId(),
                administrador.getUsuario().getPessoa().getNome(),
                administrador.getUsuario().getEmail(),
                administrador.getDataContratacao()
        );
    }
}