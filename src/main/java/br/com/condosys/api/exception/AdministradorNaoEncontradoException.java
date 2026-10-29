package br.com.condosys.api.exception;

// Exceção utilizada quando um administrador não é encontrado.
public class AdministradorNaoEncontradoException
        extends RuntimeException {

    public AdministradorNaoEncontradoException() {
        super("Administrador não encontrado.");
    }
}