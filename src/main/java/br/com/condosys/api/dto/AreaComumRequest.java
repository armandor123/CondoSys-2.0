package br.com.condosys.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Dados recebidos para cadastrar uma área comum.
public record AreaComumRequest(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(
                max = 100,
                message = "O nome deve ter no máximo 100 caracteres."
        )
        String nome,

        @NotNull(message = "A capacidade é obrigatória.")
        @Min(
                value = 0,
                message = "A capacidade não pode ser negativa."
        )
        Integer capacidade,

        String descricao

) {
}