package br.com.condosys.api.controller;

import br.com.condosys.api.domain.model.Autorizacao;
import br.com.condosys.api.dto.AutorizacaoRequest;
import br.com.condosys.api.dto.AutorizacaoResponse;
import br.com.condosys.api.mapper.AutorizacaoMapper;
import br.com.condosys.api.service.AutorizacaoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Endpoints HTTP relacionados às autorizações.
@RestController
@RequestMapping("/autorizacoes")
public class AutorizacaoController {

    private final AutorizacaoService autorizacaoService;
    private final AutorizacaoMapper autorizacaoMapper;

    public AutorizacaoController(
            AutorizacaoService autorizacaoService,
            AutorizacaoMapper autorizacaoMapper
    ) {
        this.autorizacaoService = autorizacaoService;
        this.autorizacaoMapper = autorizacaoMapper;
    }


    // POST /autorizacoes
    @PostMapping
    public ResponseEntity<AutorizacaoResponse> cadastrar(
            @Valid @RequestBody AutorizacaoRequest request
    ) {

        Autorizacao autorizacao =
                autorizacaoService.cadastrar(
                        request.pessoaId(),
                        request.proprietarioId(),
                        request.unidadeId(),
                        request.tipo(),
                        request.inicioValidade(),
                        request.fimValidade(),
                        request.observacao()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        autorizacaoMapper.toResponse(autorizacao)
                );
    }


    // GET /autorizacoes
    @GetMapping
    public List<AutorizacaoResponse> listarTodas() {

        return autorizacaoService.listarTodas()
                .stream()
                .map(autorizacaoMapper::toResponse)
                .toList();
    }


    // GET /autorizacoes/{id}
    @GetMapping("/{id}")
    public AutorizacaoResponse buscarPorId(
            @PathVariable UUID id
    ) {

        return autorizacaoMapper.toResponse(
                autorizacaoService.buscarPorId(id)
        );
    }


    // GET /autorizacoes/unidade/{unidadeId}/ativas
    @GetMapping("/unidade/{unidadeId}/ativas")
    public List<AutorizacaoResponse> listarAtivasPorUnidade(
            @PathVariable UUID unidadeId
    ) {

        return autorizacaoService
                .listarAtivasPorUnidade(unidadeId)
                .stream()
                .map(autorizacaoMapper::toResponse)
                .toList();
    }


    // PUT /autorizacoes/{id}/revogar
    @PutMapping("/{id}/revogar")
    public AutorizacaoResponse revogar(
            @PathVariable UUID id
    ) {

        return autorizacaoMapper.toResponse(
                autorizacaoService.revogar(id)
        );
    }
}