package br.com.condosys.api.exception;

// Exceção utilizada quando uma área comum não é encontrada.
public class AreaComumNaoEncontradaException
        extends RuntimeException {

    public AreaComumNaoEncontradaException() {
        super("Área comum não encontrada.");
    }
}