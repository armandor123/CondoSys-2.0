package br.com.condosys.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import br.com.condosys.api.exception.EmailJaCadastradoException;
import br.com.condosys.api.exception.PessoaJaPossuiUsuarioException;
import br.com.condosys.api.exception.UsuarioNaoEncontradoException;



import java.util.LinkedHashMap;
import java.util.Map;


// Centraliza o tratamento de erros da API.
@RestControllerAdvice
public class GlobalExceptionHandler {


    // Pessoa não encontrada -> 404.
    @ExceptionHandler(PessoaNaoEncontradaException.class)
    public ResponseEntity<Map<String, String>> tratarPessoaNaoEncontrada(
            PessoaNaoEncontradaException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "erro", exception.getMessage()
                ));
    }


    // Documento duplicado -> 409.
    @ExceptionHandler(DocumentoJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> tratarDocumentoDuplicado(
            DocumentoJaCadastradoException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "erro", exception.getMessage()
                ));
    }


    // Dados inválidos enviados no JSON -> 400.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> tratarValidacao(
            MethodArgumentNotValidException exception
    ) {

        Map<String, String> erros = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(erro ->
                        erros.put(
                                erro.getField(),
                                erro.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erros);
    }

    // Usuário não encontrado -> 404.
    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> tratarUsuarioNaoEncontrado(
            UsuarioNaoEncontradoException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "erro", exception.getMessage()
                ));
    }


    // E-mail já utilizado -> 409.
    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> tratarEmailDuplicado(
            EmailJaCadastradoException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "erro", exception.getMessage()
                ));
    }


    // Pessoa já possui usuário -> 409.
    @ExceptionHandler(PessoaJaPossuiUsuarioException.class)
    public ResponseEntity<Map<String, String>> tratarPessoaComUsuario(
            PessoaJaPossuiUsuarioException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "erro", exception.getMessage()
                ));
    }

    // Senha atual incorreta -> 400.
    @ExceptionHandler(SenhaAtualIncorretaException.class)
    public ResponseEntity<Map<String, String>> tratarSenhaIncorreta(
            SenhaAtualIncorretaException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "erro", exception.getMessage()
                ));
    }
}
