package br.com.condosys.api.dto;

import java.time.LocalDate;
import java.util.UUID;

// Dados devolvidos pela API ao consultar um administrador.
public record AdministradorResponse(

        // UUID do usuário administrador.
        UUID usuarioId,

        // Nome da pessoa vinculada ao usuário.
        String nome,

        // E-mail utilizado pelo usuário.
        String email,

        // Data de contratação do administrador.
        LocalDate dataContratacao

) {
}