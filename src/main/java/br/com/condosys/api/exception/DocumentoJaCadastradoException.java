package br.com.condosys.api.exception;

// Erro usado quando o documento já existe no sistema.
public class DocumentoJaCadastradoException extends RuntimeException {

    public DocumentoJaCadastradoException() {
        super("Já existe uma pessoa cadastrada com este documento.");
    }
}