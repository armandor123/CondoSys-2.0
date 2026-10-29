package br.com.condosys.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

// Dados recebidos para cadastrar uma unidade.
public record UnidadeRequest(

        // Proprietário responsável pela unidade.
        @NotNull(message = "O proprietário é obrigatório.")
        UUID proprietarioId,

        // Bloco ou torre da unidade.
        @NotBlank(message = "O bloco é obrigatório.")
        @Size(max = 20, message = "O bloco deve ter no máximo 20 caracteres.")
        String bloco,

        // Número da unidade.
        @NotBlank(message = "O número da unidade é obrigatório.")
        @Size(max = 20, message = "O número deve ter no máximo 20 caracteres.")
        String numero,

        // Andar da unidade.
        Integer andar

) {
}