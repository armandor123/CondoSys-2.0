package br.com.condosys.api.controller;

import br.com.condosys.api.domain.model.Visitante;
import br.com.condosys.api.dto.VisitanteObservacaoRequest;
import br.com.condosys.api.dto.VisitanteRequest;
import br.com.condosys.api.dto.VisitanteResponse;
import br.com.condosys.api.mapper.VisitanteMapper;
import br.com.condosys.api.service.VisitanteService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Endpoints HTTP relacionados aos visitantes.
@RestController
@RequestMapping("/visitantes")
public class VisitanteController {

    private final VisitanteService visitanteService;
    private final VisitanteMapper visitanteMapper;

    // Spring injeta as dependências.
    public VisitanteController(
            VisitanteService visitanteService,
            VisitanteMapper visitanteMapper
    ) {
        this.visitanteService = visitanteService;
        this.visitanteMapper = visitanteMapper;
    }


    // POST /visitantes
    @PostMapping
    public ResponseEntity<VisitanteResponse> cadastrar(
            @Valid @RequestBody VisitanteRequest request
    ) {

        Visitante visitante =
                visitanteService.cadastrar(
                        request.pessoaId(),
                        request.observacao()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        visitanteMapper.toResponse(visitante)
                );
    }


    // GET /visitantes
    @GetMapping
    public List<VisitanteResponse> listarTodos() {

        return visitanteService.listarTodos()
                .stream()
                .map(visitanteMapper::toResponse)
                .toList();
    }


    // GET /visitantes/{id}
    @GetMapping("/{id}")
    public VisitanteResponse buscarPorId(
            @PathVariable UUID id
    ) {

        Visitante visitante =
                visitanteService.buscarPorId(id);

        return visitanteMapper.toResponse(visitante);
    }


    // PUT /visitantes/{id}/observacao
    @PutMapping("/{id}/observacao")
    public VisitanteResponse atualizarObservacao(
            @PathVariable UUID id,
            @RequestBody VisitanteObservacaoRequest request
    ) {

        Visitante visitante =
                visitanteService.atualizarObservacao(
                        id,
                        request.observacao()
                );

        return visitanteMapper.toResponse(visitante);
    }
}