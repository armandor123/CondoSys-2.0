package br.com.condosys.api.controller;

import br.com.condosys.api.domain.model.Pessoa;
import br.com.condosys.api.dto.PessoaRequest;
import br.com.condosys.api.dto.PessoaResponse;
import br.com.condosys.api.dto.PessoaUpdateRequest;
import br.com.condosys.api.mapper.PessoaMapper;
import br.com.condosys.api.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


// Endpoints HTTP relacionados a Pessoa.
@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;
    private final PessoaMapper pessoaMapper;


    // Spring injeta Service e Mapper.
    public PessoaController(
            PessoaService pessoaService,
            PessoaMapper pessoaMapper
    ) {
        this.pessoaService = pessoaService;
        this.pessoaMapper = pessoaMapper;
    }


    // GET /pessoas
    @GetMapping
    public List<PessoaResponse> listarTodas() {

        return pessoaService.listarTodas()
                .stream()
                .map(pessoaMapper::toResponse)
                .toList();
    }


    // GET /pessoas/{id}
    @GetMapping("/{id}")
    public PessoaResponse buscarPorId(
            @PathVariable UUID id
    ) {

        Pessoa pessoa = pessoaService.buscarPorId(id);

        return pessoaMapper.toResponse(pessoa);
    }


    // GET /pessoas/documento/{documento}
    @GetMapping("/documento/{documento}")
    public PessoaResponse buscarPorDocumento(
            @PathVariable String documento
    ) {

        Pessoa pessoa = pessoaService.buscarPorDocumento(documento);

        return pessoaMapper.toResponse(pessoa);
    }


    // POST /pessoas
    @PostMapping
    public ResponseEntity<PessoaResponse> cadastrar(
            @Valid @RequestBody PessoaRequest request
    ) {

        Pessoa pessoa = pessoaMapper.toEntity(request);

        Pessoa pessoaSalva = pessoaService.salvar(pessoa);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pessoaMapper.toResponse(pessoaSalva));
    }


    // PUT /pessoas/{id}
    @PutMapping("/{id}")
    public PessoaResponse atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody PessoaUpdateRequest request
    ) {

        Pessoa pessoa = pessoaService.atualizar(
                id,
                request.nome(),
                request.documento(),
                request.telefone()
        );

        return pessoaMapper.toResponse(pessoa);
    }


    // PUT /pessoas/{id}/reativar
    @PutMapping("/{id}/reativar")
    public PessoaResponse reativar(
            @PathVariable UUID id
    ) {

        Pessoa pessoa = pessoaService.reativar(id);

        return pessoaMapper.toResponse(pessoa);
    }


    // DELETE /pessoas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(
            @PathVariable UUID id
    ) {

        pessoaService.desativar(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}