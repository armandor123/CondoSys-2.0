package br.com.condosys.api.exception;

// Exceção utilizada quando um visitante não é encontrado.
public class VisitanteNaoEncontradoException
        extends RuntimeException {

    public VisitanteNaoEncontradoException() {
        super("Visitante não encontrado.");
    }
}