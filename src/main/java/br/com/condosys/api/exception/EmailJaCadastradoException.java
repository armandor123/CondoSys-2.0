package br.com.condosys.api.exception;

// Usada quando o e-mail já pertence a outro usuário.
public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException() {
        super("Já existe um usuário cadastrado com este e-mail.");
    }
}