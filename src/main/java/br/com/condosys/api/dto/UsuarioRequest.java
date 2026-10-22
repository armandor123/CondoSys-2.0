package br.com.condosys.api.dto;

import br.com.condosys.api.domain.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;


// Dados recebidos para cadastrar um usuário.
public record UsuarioRequest(

        @NotNull(message = "A pessoa é obrigatória.")
        UUID pessoaId,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um e-mail válido.")
        @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(
                min = 8,
                max = 72,
                message = "A senha deve ter entre 8 e 72 caracteres."
        )
        String senha,

        @NotNull(message = "O perfil é obrigatório.")
        PerfilUsuario perfil

) {
}