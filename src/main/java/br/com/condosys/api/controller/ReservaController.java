package br.com.condosys.api.controller;

import br.com.condosys.api.domain.enums.StatusReserva;
import br.com.condosys.api.domain.model.Reserva;

import br.com.condosys.api.dto.ReservaRequest;
import br.com.condosys.api.dto.ReservaResponse;

import br.com.condosys.api.mapper.ReservaMapper;
import br.com.condosys.api.service.ReservaService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Endpoints HTTP relacionados às reservas.
@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final ReservaMapper reservaMapper;

    public ReservaController(
            ReservaService reservaService,
            ReservaMapper reservaMapper
    ) {
        this.reservaService = reservaService;
        this.reservaMapper = reservaMapper;
    }

    // POST /reservas
    @PostMapping
    public ResponseEntity<ReservaResponse> cadastrar(
            @Valid @RequestBody ReservaRequest request
    ) {

        Reserva reserva =
                reservaService.cadastrar(
                        request.unidadeId(),
                        request.areaComumId(),
                        request.usuarioId(),
                        request.inicio(),
                        request.fim(),
                        request.observacao()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservaMapper.toResponse(reserva));
    }

    // GET /reservas
    @GetMapping
    public List<ReservaResponse> listarTodas() {

        return reservaService.listarTodas()
                .stream()
                .map(reservaMapper::toResponse)
                .toList();
    }

    // GET /reservas/{id}
    @GetMapping("/{id}")
    public ReservaResponse buscarPorId(
            @PathVariable UUID id
    ) {

        return reservaMapper.toResponse(
                reservaService.buscarPorId(id)
        );
    }

    // GET /reservas/unidade/{unidadeId}
    @GetMapping("/unidade/{unidadeId}")
    public List<ReservaResponse> listarPorUnidade(
            @PathVariable UUID unidadeId
    ) {

        return reservaService
                .listarPorUnidade(unidadeId)
                .stream()
                .map(reservaMapper::toResponse)
                .toList();
    }

    // GET /reservas/area/{areaId}
    @GetMapping("/area/{areaId}")
    public List<ReservaResponse> listarPorArea(
            @PathVariable UUID areaId
    ) {

        return reservaService
                .listarPorArea(areaId)
                .stream()
                .map(reservaMapper::toResponse)
                .toList();
    }

    // GET /reservas/status/{status}
    @GetMapping("/status/{status}")
    public List<ReservaResponse> listarPorStatus(
            @PathVariable StatusReserva status
    ) {

        return reservaService
                .listarPorStatus(status)
                .stream()
                .map(reservaMapper::toResponse)
                .toList();
    }

    // PUT /reservas/{id}/cancelar
    @PutMapping("/{id}/cancelar")
    public ReservaResponse cancelar(
            @PathVariable UUID id
    ) {

        return reservaMapper.toResponse(
                reservaService.cancelar(id)
        );
    }

    // PUT /reservas/{id}/concluir
    @PutMapping("/{id}/concluir")
    public ReservaResponse concluir(
            @PathVariable UUID id
    ) {

        return reservaMapper.toResponse(
                reservaService.concluir(id)
        );
    }
}