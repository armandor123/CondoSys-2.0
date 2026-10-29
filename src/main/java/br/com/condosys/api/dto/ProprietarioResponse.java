package br.com.condosys.api.dto;

import java.time.LocalDate;
import java.util.UUID;

// Dados devolvidos pela API ao consultar um proprietário.
public record ProprietarioResponse(

        // UUID do usuário proprietário.
        UUID usuarioId,

        // Nome da pessoa vinculada ao usuário.
        String nome,

        // E-mail utilizado pelo usuário.
        String email,

        // Data de adesão do proprietário.
        LocalDate dataAdesao

) {
}