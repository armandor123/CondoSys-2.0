package br.com.condosys.api.dto;

import java.util.UUID;

// Dados devolvidos pela API ao consultar um visitante.
public record VisitanteResponse(

        // UUID compartilhado com Pessoa.
        UUID pessoaId,

        // Nome do visitante.
        String nome,

        // Documento do visitante.
        String documento,

        // Telefone do visitante.
        String telefone,

        // Observação sobre o visitante.
        String observacao

) {
}