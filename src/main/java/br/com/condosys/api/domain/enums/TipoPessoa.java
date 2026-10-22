package br.com.condosys.api.domain.enums;

// Enum representa um conjunto fechado de valores possíveis.
// Aqui definimos quais tipos de pessoa podem existir no CondoSys.
public enum TipoPessoa {

    // Pessoa que possui acesso ao sistema com login.
    USUARIO,

    // Pessoa que acessa o condomínio como visitante.
    VISITANTE,

    // Pessoa que presta algum tipo de serviço no condomínio.
    PRESTADOR_SERVICO
}
