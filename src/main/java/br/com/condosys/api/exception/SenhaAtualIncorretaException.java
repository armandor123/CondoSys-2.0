package br.com.condosys.api.exception;


// Usada quando a senha atual informada está incorreta.
public class SenhaAtualIncorretaException extends RuntimeException {

    public SenhaAtualIncorretaException() {
        super("A senha atual está incorreta.");
    }
}