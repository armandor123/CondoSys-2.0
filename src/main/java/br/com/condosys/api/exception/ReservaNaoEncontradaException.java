package br.com.condosys.api.exception;

// Exceção utilizada quando uma reserva não é encontrada.
public class ReservaNaoEncontradaException
        extends RuntimeException {

    public ReservaNaoEncontradaException() {
        super("Reserva não encontrada.");
    }
}