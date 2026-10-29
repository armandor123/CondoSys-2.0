package br.com.condosys.api.service;

import br.com.condosys.api.domain.enums.StatusAutorizacao;
import br.com.condosys.api.domain.enums.TipoAutorizacao;
import br.com.condosys.api.domain.enums.TipoPessoa;

import br.com.condosys.api.domain.model.Autorizacao;
import br.com.condosys.api.domain.model.Pessoa;
import br.com.condosys.api.domain.model.Proprietario;
import br.com.condosys.api.domain.model.Unidade;

import br.com.condosys.api.exception.AutorizacaoNaoEncontradaException;
import br.com.condosys.api.repository.AutorizacaoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

// Regras de negócio relacionadas às autorizações.
@Service
public class AutorizacaoService {

    private final AutorizacaoRepository autorizacaoRepository;
    private final PessoaService pessoaService;
    private final ProprietarioService proprietarioService;
    private final UnidadeService unidadeService;

    public AutorizacaoService(
            AutorizacaoRepository autorizacaoRepository,
            PessoaService pessoaService,
            ProprietarioService proprietarioService,
            UnidadeService unidadeService
    ) {
        this.autorizacaoRepository = autorizacaoRepository;
        this.pessoaService = pessoaService;
        this.proprietarioService = proprietarioService;
        this.unidadeService = unidadeService;
    }


    // Cadastra uma nova autorização.
    @Transactional
    public Autorizacao cadastrar(
            UUID pessoaId,
            UUID proprietarioId,
            UUID unidadeId,
            TipoAutorizacao tipo,
            OffsetDateTime inicioValidade,
            OffsetDateTime fimValidade,
            String observacao
    ) {

        Pessoa pessoa =
                pessoaService.buscarPorId(pessoaId);

        Proprietario proprietario =
                proprietarioService.buscarPorId(proprietarioId);

        Unidade unidade =
                unidadeService.buscarPorId(unidadeId);


        // Impede autorização para Pessoa inativa.
        if (!pessoa.isAtivo()) {
            throw new IllegalArgumentException(
                    "A pessoa autorizada está inativa."
            );
        }


        // A autorização é destinada a visitante ou prestador.
        if (pessoa.getTipoPessoa() == TipoPessoa.USUARIO) {
            throw new IllegalArgumentException(
                    "Usuários do sistema não necessitam de autorização de visitante."
            );
        }


        // Impede autorização para unidade desativada.
        if (!unidade.isAtiva()) {
            throw new IllegalArgumentException(
                    "A unidade está inativa."
            );
        }


        // Confirma que a unidade pertence ao proprietário informado.
        if (!unidade.getProprietario()
                .getUsuarioId()
                .equals(proprietarioId)) {

            throw new IllegalArgumentException(
                    "A unidade não pertence ao proprietário informado."
            );
        }


        Autorizacao autorizacao =
                new Autorizacao(
                        pessoa,
                        proprietario,
                        unidade,
                        tipo,
                        inicioValidade,
                        fimValidade,
                        observacao
                );

        return autorizacaoRepository.save(autorizacao);
    }


    // Lista todas as autorizações.
    @Transactional(readOnly = true)
    public List<Autorizacao> listarTodas() {

        return autorizacaoRepository.findAll();
    }


    // Busca autorização pelo UUID.
    @Transactional(readOnly = true)
    public Autorizacao buscarPorId(UUID id) {

        return autorizacaoRepository
                .findById(id)
                .orElseThrow(
                        AutorizacaoNaoEncontradaException::new
                );
    }


    // Lista autorizações ativas de uma unidade.
    @Transactional(readOnly = true)
    public List<Autorizacao> listarAtivasPorUnidade(
            UUID unidadeId
    ) {

        // Confirma que a unidade existe.
        unidadeService.buscarPorId(unidadeId);

        return autorizacaoRepository
                .findAllByUnidade_IdAndStatus(
                        unidadeId,
                        StatusAutorizacao.ATIVA
                )
                .stream()
                .filter(Autorizacao::estaValida)
                .toList();
    }


    // Revoga uma autorização.
    @Transactional
    public Autorizacao revogar(UUID id) {

        Autorizacao autorizacao =
                buscarPorId(id);

        autorizacao.revogar();

        return autorizacaoRepository.save(autorizacao);
    }
}