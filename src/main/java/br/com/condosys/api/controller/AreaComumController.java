package br.com.condosys.api.controller;

import br.com.condosys.api.domain.model.AreaComum;
import br.com.condosys.api.dto.AreaComumRequest;
import br.com.condosys.api.dto.AreaComumResponse;
import br.com.condosys.api.mapper.AreaComumMapper;
import br.com.condosys.api.service.AreaComumService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Endpoints HTTP relacionados às áreas comuns.
@RestController
@RequestMapping("/areas-comuns")
public class AreaComumController {

    private final AreaComumService areaComumService;
    private final AreaComumMapper areaComumMapper;

    public AreaComumController(
            AreaComumService areaComumService,
            AreaComumMapper areaComumMapper
    ) {
        this.areaComumService = areaComumService;
        this.areaComumMapper = areaComumMapper;
    }

    // POST /areas-comuns
    @PostMapping
    public ResponseEntity<AreaComumResponse> cadastrar(
            @Valid @RequestBody AreaComumRequest request
    ) {

        AreaComum areaComum =
                areaComumService.cadastrar(
                        request.nome(),
                        request.capacidade(),
                        request.descricao()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(areaComumMapper.toResponse(areaComum));
    }

    // GET /areas-comuns
    @GetMapping
    public List<AreaComumResponse> listarTodas() {

        return areaComumService.listarTodas()
                .stream()
                .map(areaComumMapper::toResponse)
                .toList();
    }

    // GET /areas-comuns/{id}
    @GetMapping("/{id}")
    public AreaComumResponse buscarPorId(
            @PathVariable UUID id
    ) {

        return areaComumMapper.toResponse(
                areaComumService.buscarPorId(id)
        );
    }

    // PUT /areas-comuns/{id}
    @PutMapping("/{id}")
    public AreaComumResponse atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody AreaComumRequest request
    ) {

        AreaComum areaComum =
                areaComumService.atualizar(
                        id,
                        request.nome(),
                        request.capacidade(),
                        request.descricao()
                );

        return areaComumMapper.toResponse(areaComum);
    }

    // DELETE /areas-comuns/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(
            @PathVariable UUID id
    ) {

        areaComumService.desativar(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    // PUT /areas-comuns/{id}/reativar
    @PutMapping("/{id}/reativar")
    public AreaComumResponse reativar(
            @PathVariable UUID id
    ) {

        return areaComumMapper.toResponse(
                areaComumService.reativar(id)
        );
    }
}