package br.com.condosys.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Dados recebidos para atualizar uma unidade.
public record UnidadeUpdateRequest(

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