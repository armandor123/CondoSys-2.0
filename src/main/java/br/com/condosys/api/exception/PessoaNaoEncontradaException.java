package br.com.condosys.api.exception;

// Erro usado quando uma pessoa não é encontrada.
public class PessoaNaoEncontradaException extends RuntimeException {

    public PessoaNaoEncontradaException() {
        super("Pessoa não encontrada.");
    }
}
