package br.com.condosys.api.controller;

import br.com.condosys.api.domain.model.Unidade;
import br.com.condosys.api.dto.UnidadeRequest;
import br.com.condosys.api.dto.UnidadeResponse;
import br.com.condosys.api.dto.UnidadeUpdateRequest;
import br.com.condosys.api.mapper.UnidadeMapper;
import br.com.condosys.api.service.UnidadeService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Endpoints HTTP relacionados às unidades.
@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    private final UnidadeService unidadeService;
    private final UnidadeMapper unidadeMapper;

    // Spring injeta as dependências.
    public UnidadeController(
            UnidadeService unidadeService,
            UnidadeMapper unidadeMapper
    ) {
        this.unidadeService = unidadeService;
        this.unidadeMapper = unidadeMapper;
    }


    // POST /unidades
    @PostMapping
    public ResponseEntity<UnidadeResponse> cadastrar(
            @Valid @RequestBody UnidadeRequest request
    ) {

        Unidade unidade = unidadeService.cadastrar(
                request.proprietarioId(),
                request.bloco(),
                request.numero(),
                request.andar()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(unidadeMapper.toResponse(unidade));
    }


    // GET /unidades
    @GetMapping
    public List<UnidadeResponse> listarTodas() {

        return unidadeService.listarTodas()
                .stream()
                .map(unidadeMapper::toResponse)
                .toList();
    }


    // GET /unidades/{id}
    @GetMapping("/{id}")
    public UnidadeResponse buscarPorId(
            @PathVariable UUID id
    ) {

        Unidade unidade = unidadeService.buscarPorId(id);

        return unidadeMapper.toResponse(unidade);
    }


    // PUT /unidades/{id}
    @PutMapping("/{id}")
    public UnidadeResponse atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody UnidadeUpdateRequest request
    ) {

        Unidade unidade = unidadeService.atualizar(
                id,
                request.bloco(),
                request.numero(),
                request.andar()
        );

        return unidadeMapper.toResponse(unidade);
    }


    // DELETE /unidades/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(
            @PathVariable UUID id
    ) {

        unidadeService.desativar(id);

        return ResponseEntity
                .noContent()
                .build();
    }


    // PUT /unidades/{id}/reativar
    @PutMapping("/{id}/reativar")
    public UnidadeResponse reativar(
            @PathVariable UUID id
    ) {

        Unidade unidade = unidadeService.reativar(id);

        return unidadeMapper.toResponse(unidade);
    }
}