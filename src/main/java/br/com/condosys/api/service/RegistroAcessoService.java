package br.com.condosys.api.service;

import br.com.condosys.api.domain.enums.StatusAcesso;
import br.com.condosys.api.domain.enums.TipoAutorizacao;
import br.com.condosys.api.domain.enums.TipoMovimentoAcesso;

import br.com.condosys.api.domain.model.Autorizacao;
import br.com.condosys.api.domain.model.Pessoa;
import br.com.condosys.api.domain.model.Porteiro;
import br.com.condosys.api.domain.model.RegistroAcesso;
import br.com.condosys.api.domain.model.Unidade;

import br.com.condosys.api.exception.RegistroAcessoNaoEncontradoException;
import br.com.condosys.api.repository.RegistroAcessoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

// Regras de negócio relacionadas ao controle de acesso.
@Service
public class RegistroAcessoService {

    private final RegistroAcessoRepository registroAcessoRepository;
    private final PessoaService pessoaService;
    private final UnidadeService unidadeService;
    private final PorteiroService porteiroService;
    private final AutorizacaoService autorizacaoService;


    public RegistroAcessoService(
            RegistroAcessoRepository registroAcessoRepository,
            PessoaService pessoaService,
            UnidadeService unidadeService,
            PorteiroService porteiroService,
            AutorizacaoService autorizacaoService
    ) {
        this.registroAcessoRepository = registroAcessoRepository;
        this.pessoaService = pessoaService;
        this.unidadeService = unidadeService;
        this.porteiroService = porteiroService;
        this.autorizacaoService = autorizacaoService;
    }


    // Registra uma entrada ou saída.
    @Transactional
    public RegistroAcesso registrar(
            UUID pessoaId,
            UUID unidadeId,
            UUID porteiroId,
            UUID autorizacaoId,
            TipoMovimentoAcesso tipoMovimento,
            StatusAcesso status,
            boolean liberacaoManual,
            String motivoLiberacao,
            String observacao
    ) {

        Pessoa pessoa =
                pessoaService.buscarPorId(pessoaId);

        Unidade unidade =
                unidadeService.buscarPorId(unidadeId);

        Porteiro porteiro =
                porteiroService.buscarPorId(porteiroId);


        // Impede acesso de pessoa inativa.
        if (!pessoa.isAtivo()) {
            throw new IllegalArgumentException(
                    "A pessoa está inativa."
            );
        }


        // Impede utilização de unidade inativa.
        if (!unidade.isAtiva()) {
            throw new IllegalArgumentException(
                    "A unidade está inativa."
            );
        }


        // Porteiro precisa possuir usuário ativo.
        if (!porteiro.getUsuario().isAtivo()) {
            throw new IllegalArgumentException(
                    "O porteiro está inativo."
            );
        }


        Autorizacao autorizacao = null;

        // Busca autorização quando ela for informada.
        if (autorizacaoId != null) {

            autorizacao =
                    autorizacaoService.buscarPorId(
                            autorizacaoId
                    );

            // A autorização precisa pertencer à pessoa.
            if (!autorizacao
                    .getPessoa()
                    .getId()
                    .equals(pessoaId)) {

                throw new IllegalArgumentException(
                        "A autorização não pertence à pessoa informada."
                );
            }


            // A autorização precisa pertencer à unidade.
            if (!autorizacao
                    .getUnidade()
                    .getId()
                    .equals(unidadeId)) {

                throw new IllegalArgumentException(
                        "A autorização não pertence à unidade informada."
                );
            }
        }


        // Acesso negado não representa liberação manual.
        if (status == StatusAcesso.NEGADO &&
                liberacaoManual) {

            throw new IllegalArgumentException(
                    "Um acesso negado não pode possuir liberação manual."
            );
        }


        // Acesso autorizado normalmente precisa de autorização válida.
        // Acesso autorizado normalmente precisa de autorização válida.
        if (
                status == StatusAcesso.AUTORIZADO
                        && tipoMovimento == TipoMovimentoAcesso.ENTRADA
                        && !liberacaoManual
        ) {

            if (autorizacao == null) {
                throw new IllegalArgumentException(
                        "Uma autorização válida é obrigatória para registrar a entrada."
                );
            }

            if (!autorizacao.estaValida()) {
                throw new IllegalArgumentException(
                        "A autorização informada não está válida."
                );
            }
        }


        // Liberação manual precisa possuir justificativa.
        if (liberacaoManual &&
                (motivoLiberacao == null ||
                        motivoLiberacao.isBlank())) {

            throw new IllegalArgumentException(
                    "Informe o motivo da liberação manual."
            );
        }


        RegistroAcesso registro =
                new RegistroAcesso(
                        pessoa,
                        unidade,
                        porteiro,
                        autorizacao,
                        tipoMovimento,
                        status,
                        liberacaoManual,
                        motivoLiberacao,
                        observacao
                );


        RegistroAcesso registroSalvo =
                registroAcessoRepository.save(registro);


        // Consome uma autorização de entrada única.
        if (status == StatusAcesso.AUTORIZADO &&
                tipoMovimento == TipoMovimentoAcesso.ENTRADA &&
                !liberacaoManual &&
                autorizacao != null &&
                autorizacao.getTipo()
                        == TipoAutorizacao.UNICA_ENTRADA) {

            autorizacao.marcarComoUtilizada();
        }


        return registroSalvo;
    }


    // Lista todos os registros.
    @Transactional(readOnly = true)
    public List<RegistroAcesso> listarTodos() {

        return registroAcessoRepository.findAll();
    }


    // Busca um registro pelo UUID.
    @Transactional(readOnly = true)
    public RegistroAcesso buscarPorId(UUID id) {

        return registroAcessoRepository
                .findById(id)
                .orElseThrow(
                        RegistroAcessoNaoEncontradoException::new
                );
    }


    // Lista os registros de uma unidade.
    @Transactional(readOnly = true)
    public List<RegistroAcesso> listarPorUnidade(
            UUID unidadeId
    ) {

        unidadeService.buscarPorId(unidadeId);

        return registroAcessoRepository
                .findAllByUnidade_IdOrderByDataHoraDesc(
                        unidadeId
                );
    }


    // Lista os registros de uma pessoa.
    @Transactional(readOnly = true)
    public List<RegistroAcesso> listarPorPessoa(
            UUID pessoaId
    ) {

        pessoaService.buscarPorId(pessoaId);

        return registroAcessoRepository
                .findAllByPessoa_IdOrderByDataHoraDesc(
                        pessoaId
                );
    }
}