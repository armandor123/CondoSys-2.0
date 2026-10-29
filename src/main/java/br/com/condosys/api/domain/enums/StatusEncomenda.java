package br.com.condosys.api.domain.enums;

// Define a situação atual de uma encomenda.
public enum StatusEncomenda {

    // Encomenda acabou de ser recebida pela portaria.
    RECEBIDA,

    // Encomenda está aguardando retirada.
    AGUARDANDO_RETIRADA,

    // Encomenda já foi entregue.
    ENTREGUE
}