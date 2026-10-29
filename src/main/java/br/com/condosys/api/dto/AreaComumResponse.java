package br.com.condosys.api.dto;

import java.util.UUID;

// Dados devolvidos pela API sobre uma área comum.
public record AreaComumResponse(

        UUID id,
        String nome,
        Integer capacidade,
        boolean ativa,
        String descricao

) {
}