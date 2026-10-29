package br.com.condosys.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.UUID;

// Dados recebidos para cadastrar um proprietário.
public record ProprietarioRequest(

        // Usuário que será associado ao proprietário.
        @NotNull(message = "O usuário é obrigatório.")
        UUID usuarioId,

        // Data de adesão do proprietário ao sistema.
        @NotNull(message = "A data de adesão é obrigatória.")
        @PastOrPresent(message = "A data de adesão não pode ser futura.")
        LocalDate dataAdesao

) {
}