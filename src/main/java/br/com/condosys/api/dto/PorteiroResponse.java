package br.com.condosys.api.dto;

import java.time.LocalDate;
import java.util.UUID;

// Dados devolvidos pela API ao consultar um porteiro.
public record PorteiroResponse(

        // UUID do usuário porteiro.
        UUID usuarioId,

        // Nome da pessoa vinculada ao usuário.
        String nome,

        // E-mail utilizado pelo usuário.
        String email,

        // Turno de trabalho.
        String turno,

        // Data de demissão, quando existir.
        LocalDate dataDemissao

) {
}