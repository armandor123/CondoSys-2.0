package br.com.condosys.api.exception;

// Exceção utilizada quando uma encomenda não é encontrada.
public class EncomendaNaoEncontradaException
        extends RuntimeException {

    public EncomendaNaoEncontradaException() {
        super("Encomenda não encontrada.");
    }
}