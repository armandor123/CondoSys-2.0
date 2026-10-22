package br.com.condosys.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


// Dados permitidos na atualização de uma pessoa.
public record PessoaUpdateRequest(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
        String nome,

        @NotBlank(message = "O documento é obrigatório.")
        @Size(max = 30, message = "O documento deve ter no máximo 30 caracteres.")
        String documento,

        @Size(max = 30, message = "O telefone deve ter no máximo 30 caracteres.")
        String telefone

) {
}