package br.com.condosys.api.dto;

import br.com.condosys.api.domain.enums.PerfilUsuario;

import java.time.OffsetDateTime;
import java.util.UUID;


// Dados de Usuario devolvidos pela API.
public record UsuarioResponse(

        UUID id,
        UUID pessoaId,
        String email,
        PerfilUsuario perfil,
        boolean ativo,
        OffsetDateTime criadoEm,
        OffsetDateTime atualizadoEm

) {
}