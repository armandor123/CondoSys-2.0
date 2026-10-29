package br.com.condosys.api.exception;

// Exceção utilizada quando uma unidade não é encontrada.
public class UnidadeNaoEncontradaException
        extends RuntimeException {

    public UnidadeNaoEncontradaException() {
        super("Unidade não encontrada.");
    }
}