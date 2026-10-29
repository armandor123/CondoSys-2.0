package br.com.condosys.api.controller;

import br.com.condosys.api.domain.model.Administrador;
import br.com.condosys.api.dto.AdministradorRequest;
import br.com.condosys.api.dto.AdministradorResponse;
import br.com.condosys.api.mapper.AdministradorMapper;
import br.com.condosys.api.service.AdministradorService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Endpoints HTTP relacionados aos administradores.
@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    private final AdministradorService administradorService;
    private final AdministradorMapper administradorMapper;

    // Spring injeta as dependências.
    public AdministradorController(
            AdministradorService administradorService,
            AdministradorMapper administradorMapper
    ) {
        this.administradorService = administradorService;
        this.administradorMapper = administradorMapper;
    }


    // POST /administradores
    @PostMapping
    public ResponseEntity<AdministradorResponse> cadastrar(
            @Valid @RequestBody AdministradorRequest request
    ) {

        Administrador administrador =
                administradorService.cadastrar(
                        request.usuarioId(),
                        request.dataContratacao()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        administradorMapper.toResponse(administrador)
                );
    }


    // GET /administradores
    @GetMapping
    public List<AdministradorResponse> listarTodos() {

        return administradorService.listarTodos()
                .stream()
                .map(administradorMapper::toResponse)
                .toList();
    }


    // GET /administradores/{id}
    @GetMapping("/{id}")
    public AdministradorResponse buscarPorId(
            @PathVariable UUID id
    ) {

        Administrador administrador =
                administradorService.buscarPorId(id);

        return administradorMapper.toResponse(administrador);
    }
}