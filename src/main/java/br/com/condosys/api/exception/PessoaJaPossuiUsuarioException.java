package br.com.condosys.api.exception;

// Impede que a mesma Pessoa tenha dois usuários.
public class PessoaJaPossuiUsuarioException extends RuntimeException {

    public PessoaJaPossuiUsuarioException() {
        super("Esta pessoa já possui um usuário cadastrado.");
    }
}