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

    // Administrador não encontrado -> HTTP 404.
    @ExceptionHandler(AdministradorNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> tratarAdministradorNaoEncontrado(
            AdministradorNaoEncontradoException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "erro", exception.getMessage()
                ));
    }

    // Porteiro não encontrado -> HTTP 404.
    @ExceptionHandler(PorteiroNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> tratarPorteiroNaoEncontrado(
            PorteiroNaoEncontradoException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "erro", exception.getMessage()
                ));
    }
    // Proprietário não encontrado -> HTTP 404.
    @ExceptionHandler(ProprietarioNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> tratarProprietarioNaoEncontrado(
            ProprietarioNaoEncontradoException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "erro", exception.getMessage()
                ));
    }
    // Unidade não encontrada -> HTTP 404.
    @ExceptionHandler(UnidadeNaoEncontradaException.class)
    public ResponseEntity<Map<String, String>> tratarUnidadeNaoEncontrada(
            UnidadeNaoEncontradaException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "erro", exception.getMessage()
                ));
    }

    // Visitante não encontrado -> HTTP 404.
    @ExceptionHandler(VisitanteNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> tratarVisitanteNaoEncontrado(
            VisitanteNaoEncontradoException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "erro", exception.getMessage()
                ));
    }

    // Autorização não encontrada -> HTTP 404.
    @ExceptionHandler(AutorizacaoNaoEncontradaException.class)
    public ResponseEntity<Map<String, String>> tratarAutorizacaoNaoEncontrada(
            AutorizacaoNaoEncontradaException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "erro", exception.getMessage()
                ));
    }

    // Registro de acesso não encontrado -> HTTP 404.
    @ExceptionHandler(RegistroAcessoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> tratarRegistroAcessoNaoEncontrado(
            RegistroAcessoNaoEncontradoException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "erro", exception.getMessage()
                ));
    }

    // Encomenda não encontrada -> HTTP 404.
    @ExceptionHandler(EncomendaNaoEncontradaException.class)
    public ResponseEntity<Map<String, String>> tratarEncomendaNaoEncontrada(
            EncomendaNaoEncontradaException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "erro", exception.getMessage()
                ));
    }

    // Área comum não encontrada -> HTTP 404.
    @ExceptionHandler(AreaComumNaoEncontradaException.class)
    public ResponseEntity<Map<String, String>> tratarAreaComumNaoEncontrada(
            AreaComumNaoEncontradaException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "erro", exception.getMessage()
                ));
    }


    // Reserva não encontrada -> HTTP 404.
    @ExceptionHandler(ReservaNaoEncontradaException.class)
    public ResponseEntity<Map<String, String>> tratarReservaNaoEncontrada(
            ReservaNaoEncontradaException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "erro", exception.getMessage()
                ));
    }
}
