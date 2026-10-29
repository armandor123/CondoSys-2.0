package br.com.condosys.api.exception;

// Exceção utilizada quando um registro de acesso não é encontrado.
public class RegistroAcessoNaoEncontradoException
        extends RuntimeException {

    public RegistroAcessoNaoEncontradoException() {
        super("Registro de acesso não encontrado.");
    }
}