package br.com.condosys.api.exception;

// Exceção utilizada quando um porteiro não é encontrado.
public class PorteiroNaoEncontradoException
        extends RuntimeException {

    public PorteiroNaoEncontradoException() {
        super("Porteiro não encontrado.");
    }
}