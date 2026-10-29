package br.com.condosys.api.exception;

// Exceção utilizada quando uma autorização não é encontrada.
public class AutorizacaoNaoEncontradaException
        extends RuntimeException {

    public AutorizacaoNaoEncontradaException() {
        super("Autorização não encontrada.");
    }
}