package br.com.condosys.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

// Dados recebidos para cadastrar um porteiro.
public record PorteiroRequest(

        // Usuário que será associado ao porteiro.
        @NotNull(message = "O usuário é obrigatório.")
        UUID usuarioId,

        // Turno de trabalho do porteiro.
        @NotBlank(message = "O turno é obrigatório.")
        @Size(max = 30, message = "O turno deve ter no máximo 30 caracteres.")
        String turno

) {
}