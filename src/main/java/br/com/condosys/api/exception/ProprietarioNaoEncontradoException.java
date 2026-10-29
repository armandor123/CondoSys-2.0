package br.com.condosys.api.exception;

// Exceção utilizada quando um proprietário não é encontrado.
public class ProprietarioNaoEncontradoException
        extends RuntimeException {

    public ProprietarioNaoEncontradoException() {
        super("Proprietário não encontrado.");
    }
}