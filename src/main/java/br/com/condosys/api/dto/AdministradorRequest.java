package br.com.condosys.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.UUID;

// Dados recebidos para cadastrar um administrador.
public record AdministradorRequest(

        // Usuário que será associado ao administrador.
        @NotNull(message = "O usuário é obrigatório.")
        UUID usuarioId,

        // Data de contratação do administrador.
        @NotNull(message = "A data de contratação é obrigatória.")
        @PastOrPresent(message = "A data de contratação não pode ser futura.")
        LocalDate dataContratacao

) {
}