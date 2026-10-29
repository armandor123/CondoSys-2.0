package br.com.condosys.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Dados recebidos para alterar o turno de um porteiro.
public record PorteiroTurnoRequest(

        // Novo turno de trabalho.
        @NotBlank(message = "O turno é obrigatório.")
        @Size(max = 30, message = "O turno deve ter no máximo 30 caracteres.")
        String turno

) {
}