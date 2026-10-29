package br.com.condosys.api.controller;

import br.com.condosys.api.domain.enums.StatusEncomenda;
import br.com.condosys.api.domain.model.Encomenda;

import br.com.condosys.api.dto.EncomendaEntregaRequest;
import br.com.condosys.api.dto.EncomendaRequest;
import br.com.condosys.api.dto.EncomendaResponse;

import br.com.condosys.api.mapper.EncomendaMapper;
import br.com.condosys.api.service.EncomendaService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Endpoints HTTP relacionados às encomendas.
@RestController
@RequestMapping("/encomendas")
public class EncomendaController {

    private final EncomendaService encomendaService;
    private final EncomendaMapper encomendaMapper;


    public EncomendaController(
            EncomendaService encomendaService,
            EncomendaMapper encomendaMapper
    ) {
        this.encomendaService = encomendaService;
        this.encomendaMapper = encomendaMapper;
    }


    // POST /encomendas
    @PostMapping
    public ResponseEntity<EncomendaResponse> cadastrar(
            @Valid @RequestBody EncomendaRequest request
    ) {

        Encomenda encomenda =
                encomendaService.cadastrar(
                        request.unidadeId(),
                        request.porteiroId(),
                        request.remetente(),
                        request.destinatario(),
                        request.codigoRastreio(),
                        request.descricao()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        encomendaMapper.toResponse(encomenda)
                );
    }


    // GET /encomendas
    @GetMapping
    public List<EncomendaResponse> listarTodas() {

        return encomendaService.listarTodas()
                .stream()
                .map(encomendaMapper::toResponse)
                .toList();
    }


    // GET /encomendas/{id}
    @GetMapping("/{id}")
    public EncomendaResponse buscarPorId(
            @PathVariable UUID id
    ) {

        return encomendaMapper.toResponse(
                encomendaService.buscarPorId(id)
        );
    }


    // GET /encomendas/unidade/{unidadeId}
    @GetMapping("/unidade/{unidadeId}")
    public List<EncomendaResponse> listarPorUnidade(
            @PathVariable UUID unidadeId
    ) {

        return encomendaService
                .listarPorUnidade(unidadeId)
                .stream()
                .map(encomendaMapper::toResponse)
                .toList();
    }


    // GET /encomendas/status/{status}
    @GetMapping("/status/{status}")
    public List<EncomendaResponse> listarPorStatus(
            @PathVariable StatusEncomenda status
    ) {

        return encomendaService
                .listarPorStatus(status)
                .stream()
                .map(encomendaMapper::toResponse)
                .toList();
    }


    // PUT /encomendas/{id}/aguardando-retirada
    @PutMapping("/{id}/aguardando-retirada")
    public EncomendaResponse marcarAguardandoRetirada(
            @PathVariable UUID id
    ) {

        return encomendaMapper.toResponse(
                encomendaService
                        .marcarAguardandoRetirada(id)
        );
    }


    // PUT /encomendas/{id}/entregar
    @PutMapping("/{id}/entregar")
    public EncomendaResponse entregar(
            @PathVariable UUID id,
            @Valid @RequestBody EncomendaEntregaRequest request
    ) {

        return encomendaMapper.toResponse(
                encomendaService.entregar(
                        id,
                        request.retiradoPor()
                )
        );
    }
}