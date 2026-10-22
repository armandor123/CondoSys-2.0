package br.com.condosys.api.controller;

import br.com.condosys.api.domain.model.Usuario;
import br.com.condosys.api.dto.UsuarioRequest;
import br.com.condosys.api.dto.UsuarioResponse;
import br.com.condosys.api.mapper.UsuarioMapper;
import br.com.condosys.api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.condosys.api.dto.UsuarioUpdateRequest;
import br.com.condosys.api.dto.AlterarSenhaRequest;


import java.util.UUID;
import java.util.List;


// Endpoints HTTP relacionados a Usuario.
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;


    // Spring injeta Service e Mapper.
    public UsuarioController(
            UsuarioService usuarioService,
            UsuarioMapper usuarioMapper
    ) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }


    // POST /usuarios
    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(
            @Valid @RequestBody UsuarioRequest request
    ) {

        Usuario usuario = usuarioService.cadastrar(
                request.pessoaId(),
                request.email(),
                request.senha(),
                request.perfil()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioMapper.toResponse(usuario));
    }


    // GET /usuarios
    @GetMapping
    public List<UsuarioResponse> listarTodos() {

        return usuarioService.listarTodos()
                .stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }

    // GET /usuarios/{id}
// Busca um usuário pelo UUID.
    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(
            @PathVariable UUID id
    ) {

        Usuario usuario = usuarioService.buscarPorId(id);

        return usuarioMapper.toResponse(usuario);
    }

    // PUT /usuarios/{id}
// Atualiza e-mail e perfil.
    @PutMapping("/{id}")
    public UsuarioResponse atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody UsuarioUpdateRequest request
    ) {

        Usuario usuario = usuarioService.atualizar(
                id,
                request.email(),
                request.perfil()
        );

        return usuarioMapper.toResponse(usuario);
    }

    // DELETE /usuarios/{id}
// Desativa o usuário e retorna HTTP 204.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(
            @PathVariable UUID id
    ) {

        usuarioService.desativar(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    // PUT /usuarios/{id}/reativar
    @PutMapping("/{id}/reativar")
    public UsuarioResponse reativar(
            @PathVariable UUID id
    ) {

        Usuario usuario = usuarioService.reativar(id);

        return usuarioMapper.toResponse(usuario);
    }



    // PUT /usuarios/{id}/senha
// Altera a senha após validar a senha atual.
    @PutMapping("/{id}/senha")
    public ResponseEntity<Void> alterarSenha(
            @PathVariable UUID id,
            @Valid @RequestBody AlterarSenhaRequest request
    ) {

        usuarioService.alterarSenha(
                id,
                request.senhaAtual(),
                request.novaSenha()
        );

        return ResponseEntity
                .noContent()
                .build();
    }
    // GET /usuarios/email/{email}
// Consulta um usuário pelo e-mail.
    @GetMapping("/email/{email}")
    public UsuarioResponse buscarPorEmail(
            @PathVariable String email
    ) {

        Usuario usuario = usuarioService.buscarPorEmail(email);

        return usuarioMapper.toResponse(usuario);
    }
}