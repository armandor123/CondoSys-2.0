package br.com.condosys.api.dto;

import java.util.UUID;

// Dados devolvidos pela API ao consultar uma unidade.
public record UnidadeResponse(

        // UUID da unidade.
        UUID id,

        // UUID do proprietário responsável.
        UUID proprietarioId,

        // Nome do proprietário.
        String proprietarioNome,

        // Bloco ou torre.
        String bloco,

        // Número da unidade.
        String numero,

        // Andar.
        Integer andar,

        // Indica se a unidade está ativa.
        boolean ativa

) {
}