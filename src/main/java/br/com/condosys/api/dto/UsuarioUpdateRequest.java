package br.com.condosys.api.dto;

import br.com.condosys.api.domain.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


// Dados permitidos na atualização do usuário.
public record UsuarioUpdateRequest(

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um e-mail válido.")
        @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres.")
        String email,

        @NotNull(message = "O perfil é obrigatório.")
        PerfilUsuario perfil

) {
}
