package br.com.condosys.api.controller;

import br.com.condosys.api.domain.model.RegistroAcesso;
import br.com.condosys.api.dto.RegistroAcessoRequest;
import br.com.condosys.api.dto.RegistroAcessoResponse;
import br.com.condosys.api.mapper.RegistroAcessoMapper;
import br.com.condosys.api.service.RegistroAcessoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Endpoints HTTP relacionados ao controle de acesso.
@RestController
@RequestMapping("/registros-acesso")
public class RegistroAcessoController {

    private final RegistroAcessoService registroAcessoService;
    private final RegistroAcessoMapper registroAcessoMapper;

    public RegistroAcessoController(
            RegistroAcessoService registroAcessoService,
            RegistroAcessoMapper registroAcessoMapper
    ) {
        this.registroAcessoService = registroAcessoService;
        this.registroAcessoMapper = registroAcessoMapper;
    }


    // POST /registros-acesso
    @PostMapping
    public ResponseEntity<RegistroAcessoResponse> registrar(
            @Valid @RequestBody RegistroAcessoRequest request
    ) {

        RegistroAcesso registro =
                registroAcessoService.registrar(
                        request.pessoaId(),
                        request.unidadeId(),
                        request.porteiroId(),
                        request.autorizacaoId(),
                        request.tipoMovimento(),
                        request.status(),
                        request.liberacaoManual(),
                        request.motivoLiberacao(),
                        request.observacao()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        registroAcessoMapper.toResponse(registro)
                );
    }


    // GET /registros-acesso
    @GetMapping
    public List<RegistroAcessoResponse> listarTodos() {

        return registroAcessoService.listarTodos()
                .stream()
                .map(registroAcessoMapper::toResponse)
                .toList();
    }


    // GET /registros-acesso/{id}
    @GetMapping("/{id}")
    public RegistroAcessoResponse buscarPorId(
            @PathVariable UUID id
    ) {

        return registroAcessoMapper.toResponse(
                registroAcessoService.buscarPorId(id)
        );
    }


    // GET /registros-acesso/unidade/{unidadeId}
    @GetMapping("/unidade/{unidadeId}")
    public List<RegistroAcessoResponse> listarPorUnidade(
            @PathVariable UUID unidadeId
    ) {

        return registroAcessoService
                .listarPorUnidade(unidadeId)
                .stream()
                .map(registroAcessoMapper::toResponse)
                .toList();
    }


    // GET /registros-acesso/pessoa/{pessoaId}
    @GetMapping("/pessoa/{pessoaId}")
    public List<RegistroAcessoResponse> listarPorPessoa(
            @PathVariable UUID pessoaId
    ) {

        return registroAcessoService
                .listarPorPessoa(pessoaId)
                .stream()
                .map(registroAcessoMapper::toResponse)
                .toList();
    }
}