package br.com.condosys.api.domain.enums;

// Define os tipos de autorização de acesso.
public enum TipoAutorizacao {

    // Permite somente uma entrada.
    UNICA_ENTRADA,

    // Possui data de início e fim.
    PRAZO_DETERMINADO,

    // Não possui data final definida.
    PRAZO_INDETERMINADO
}