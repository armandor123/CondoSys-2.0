package br.com.condosys.api.controller;

import br.com.condosys.api.domain.model.Proprietario;
import br.com.condosys.api.dto.ProprietarioRequest;
import br.com.condosys.api.dto.ProprietarioResponse;
import br.com.condosys.api.mapper.ProprietarioMapper;
import br.com.condosys.api.service.ProprietarioService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Endpoints HTTP relacionados aos proprietários.
@RestController
@RequestMapping("/proprietarios")
public class ProprietarioController {

    private final ProprietarioService proprietarioService;
    private final ProprietarioMapper proprietarioMapper;

    // Spring injeta as dependências.
    public ProprietarioController(
            ProprietarioService proprietarioService,
            ProprietarioMapper proprietarioMapper
    ) {
        this.proprietarioService = proprietarioService;
        this.proprietarioMapper = proprietarioMapper;
    }


    // POST /proprietarios
    @PostMapping
    public ResponseEntity<ProprietarioResponse> cadastrar(
            @Valid @RequestBody ProprietarioRequest request
    ) {

        Proprietario proprietario =
                proprietarioService.cadastrar(
                        request.usuarioId(),
                        request.dataAdesao()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        proprietarioMapper.toResponse(proprietario)
                );
    }


    // GET /proprietarios
    @GetMapping
    public List<ProprietarioResponse> listarTodos() {

        return proprietarioService.listarTodos()
                .stream()
                .map(proprietarioMapper::toResponse)
                .toList();
    }


    // GET /proprietarios/{id}
    @GetMapping("/{id}")
    public ProprietarioResponse buscarPorId(
            @PathVariable UUID id
    ) {

        Proprietario proprietario =
                proprietarioService.buscarPorId(id);

        return proprietarioMapper.toResponse(proprietario);
    }
}