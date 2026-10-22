package br.com.condosys.api.exception;

// Usada quando um usuário não é encontrado.
public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException() {
        super("Usuário não encontrado.");
    }
}