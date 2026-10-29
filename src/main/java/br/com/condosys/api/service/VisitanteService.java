package br.com.condosys.api.service;

import br.com.condosys.api.domain.enums.TipoPessoa;
import br.com.condosys.api.domain.model.Pessoa;
import br.com.condosys.api.domain.model.Visitante;
import br.com.condosys.api.exception.VisitanteNaoEncontradoException;
import br.com.condosys.api.repository.VisitanteRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

// Regras de negócio relacionadas aos visitantes.
@Service
public class VisitanteService {

    private final VisitanteRepository visitanteRepository;
    private final PessoaService pessoaService;

    // Spring injeta as dependências.
    public VisitanteService(
            VisitanteRepository visitanteRepository,
            PessoaService pessoaService
    ) {
        this.visitanteRepository = visitanteRepository;
        this.pessoaService = pessoaService;
    }


    // Cadastra uma Pessoa existente como visitante.
    @Transactional
    public Visitante cadastrar(
            UUID pessoaId,
            String observacao
    ) {

        // Confirma que a Pessoa existe.
        Pessoa pessoa = pessoaService.buscarPorId(pessoaId);

        // Exige que a Pessoa seja do tipo VISITANTE.
        if (pessoa.getTipoPessoa() != TipoPessoa.VISITANTE) {
            throw new IllegalArgumentException(
                    "A pessoa não possui tipo VISITANTE."
            );
        }

        // Impede cadastrar uma Pessoa inativa.
        if (!pessoa.isAtivo()) {
            throw new IllegalArgumentException(
                    "A pessoa está inativa."
            );
        }

        // Impede cadastro duplicado.
        if (visitanteRepository.existsById(pessoaId)) {
            throw new IllegalStateException(
                    "Esta pessoa já possui cadastro de visitante."
            );
        }

        Visitante visitante =
                new Visitante(
                        pessoa,
                        observacao
                );

        return visitanteRepository.save(visitante);
    }


    // Lista todos os visitantes cadastrados.
    @Transactional(readOnly = true)
    public List<Visitante> listarTodos() {

        return visitanteRepository.findAll();
    }


    // Busca um visitante pelo UUID da Pessoa.
    @Transactional(readOnly = true)
    public Visitante buscarPorId(UUID pessoaId) {

        return visitanteRepository
                .findById(pessoaId)
                .orElseThrow(
                        VisitanteNaoEncontradoException::new
                );
    }


    // Atualiza a observação do visitante.
    @Transactional
    public Visitante atualizarObservacao(
            UUID pessoaId,
            String observacao
    ) {

        Visitante visitante =
                buscarPorId(pessoaId);

        visitante.atualizarObservacao(observacao);

        return visitanteRepository.save(visitante);
    }
}