package br.com.condosys.api.mapper;

import br.com.condosys.api.domain.model.Usuario;
import br.com.condosys.api.dto.UsuarioResponse;
import org.springframework.stereotype.Component;


// Converte Usuario para o DTO devolvido pela API.
@Component
public class UsuarioMapper {

    public UsuarioResponse toResponse(Usuario usuario) {

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getPessoa().getId(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.isAtivo(),
                usuario.getCriadoEm(),
                usuario.getAtualizadoEm()
        );
    }
}