package br.com.condosys.api.controller;

import br.com.condosys.api.domain.model.Porteiro;
import br.com.condosys.api.dto.PorteiroDemissaoRequest;
import br.com.condosys.api.dto.PorteiroRequest;
import br.com.condosys.api.dto.PorteiroResponse;
import br.com.condosys.api.dto.PorteiroTurnoRequest;
import br.com.condosys.api.mapper.PorteiroMapper;
import br.com.condosys.api.service.PorteiroService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Endpoints HTTP relacionados aos porteiros.
@RestController
@RequestMapping("/porteiros")
public class PorteiroController {

    private final PorteiroService porteiroService;
    private final PorteiroMapper porteiroMapper;

    // Spring injeta as dependências.
    public PorteiroController(
            PorteiroService porteiroService,
            PorteiroMapper porteiroMapper
    ) {
        this.porteiroService = porteiroService;
        this.porteiroMapper = porteiroMapper;
    }


    // POST /porteiros
    @PostMapping
    public ResponseEntity<PorteiroResponse> cadastrar(
            @Valid @RequestBody PorteiroRequest request
    ) {

        Porteiro porteiro = porteiroService.cadastrar(
                request.usuarioId(),
                request.turno()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(porteiroMapper.toResponse(porteiro));
    }


    // GET /porteiros
    @GetMapping
    public List<PorteiroResponse> listarTodos() {

        return porteiroService.listarTodos()
                .stream()
                .map(porteiroMapper::toResponse)
                .toList();
    }


    // GET /porteiros/{id}
    @GetMapping("/{id}")
    public PorteiroResponse buscarPorId(
            @PathVariable UUID id
    ) {

        Porteiro porteiro =
                porteiroService.buscarPorId(id);

        return porteiroMapper.toResponse(porteiro);
    }


    // PUT /porteiros/{id}/turno
    @PutMapping("/{id}/turno")
    public PorteiroResponse atualizarTurno(
            @PathVariable UUID id,
            @Valid @RequestBody PorteiroTurnoRequest request
    ) {

        Porteiro porteiro =
                porteiroService.atualizarTurno(
                        id,
                        request.turno()
                );

        return porteiroMapper.toResponse(porteiro);
    }


    // PUT /porteiros/{id}/demissao
    @PutMapping("/{id}/demissao")
    public PorteiroResponse registrarDemissao(
            @PathVariable UUID id,
            @Valid @RequestBody PorteiroDemissaoRequest request
    ) {

        Porteiro porteiro =
                porteiroService.registrarDemissao(
                        id,
                        request.dataDemissao()
                );

        return porteiroMapper.toResponse(porteiro);
    }
}