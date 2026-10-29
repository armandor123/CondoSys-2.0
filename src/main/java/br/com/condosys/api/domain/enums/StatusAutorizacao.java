package br.com.condosys.api.domain.enums;

// Define a situação atual da autorização.
public enum StatusAutorizacao {

    // Autorização disponível para utilização.
    ATIVA,

    // O período de validade terminou.
    EXPIRADA,

    // Foi cancelada pelo responsável.
    REVOGADA,

    // Autorização de entrada única já utilizada.
    UTILIZADA
}