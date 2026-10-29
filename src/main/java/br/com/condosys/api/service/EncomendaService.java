package br.com.condosys.api.service;

import br.com.condosys.api.domain.enums.StatusEncomenda;
import br.com.condosys.api.domain.model.Encomenda;
import br.com.condosys.api.domain.model.Porteiro;
import br.com.condosys.api.domain.model.Unidade;

import br.com.condosys.api.exception.EncomendaNaoEncontradaException;
import br.com.condosys.api.repository.EncomendaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

// Regras de negócio relacionadas às encomendas.
@Service
public class EncomendaService {

    private final EncomendaRepository encomendaRepository;
    private final UnidadeService unidadeService;
    private final PorteiroService porteiroService;


    public EncomendaService(
            EncomendaRepository encomendaRepository,
            UnidadeService unidadeService,
            PorteiroService porteiroService
    ) {
        this.encomendaRepository = encomendaRepository;
        this.unidadeService = unidadeService;
        this.porteiroService = porteiroService;
    }


    // Registra uma encomenda recebida pela portaria.
    @Transactional
    public Encomenda cadastrar(
            UUID unidadeId,
            UUID porteiroId,
            String remetente,
            String destinatario,
            String codigoRastreio,
            String descricao
    ) {

        Unidade unidade =
                unidadeService.buscarPorId(unidadeId);

        Porteiro porteiro =
                porteiroService.buscarPorId(porteiroId);


        // Impede recebimento para unidade inativa.
        if (!unidade.isAtiva()) {
            throw new IllegalArgumentException(
                    "A unidade está inativa."
            );
        }


        // Porteiro precisa estar com usuário ativo.
        if (!porteiro.getUsuario().isAtivo()) {
            throw new IllegalArgumentException(
                    "O porteiro está inativo."
            );
        }


        // Porteiro demitido não pode registrar encomenda.
        if (porteiro.getDataDemissao() != null) {
            throw new IllegalArgumentException(
                    "O porteiro possui demissão registrada."
            );
        }


        Encomenda encomenda =
                new Encomenda(
                        unidade,
                        porteiro,
                        remetente,
                        destinatario,
                        codigoRastreio,
                        descricao
                );

        return encomendaRepository.save(encomenda);
    }


    // Lista todas as encomendas.
    @Transactional(readOnly = true)
    public List<Encomenda> listarTodas() {

        return encomendaRepository.findAll();
    }


    // Busca encomenda pelo UUID.
    @Transactional(readOnly = true)
    public Encomenda buscarPorId(UUID id) {

        return encomendaRepository
                .findById(id)
                .orElseThrow(
                        EncomendaNaoEncontradaException::new
                );
    }


    // Lista encomendas de uma unidade.
    @Transactional(readOnly = true)
    public List<Encomenda> listarPorUnidade(
            UUID unidadeId
    ) {

        unidadeService.buscarPorId(unidadeId);

        return encomendaRepository
                .findAllByUnidade_IdOrderByRecebidoEmDesc(
                        unidadeId
                );
    }


    // Lista encomendas por status.
    @Transactional(readOnly = true)
    public List<Encomenda> listarPorStatus(
            StatusEncomenda status
    ) {

        return encomendaRepository
                .findAllByStatusOrderByRecebidoEmDesc(
                        status
                );
    }


    // Marca como aguardando retirada.
    @Transactional
    public Encomenda marcarAguardandoRetirada(
            UUID id
    ) {

        Encomenda encomenda =
                buscarPorId(id);

        encomenda.marcarAguardandoRetirada();

        return encomendaRepository.save(encomenda);
    }


    // Registra a retirada da encomenda.
    @Transactional
    public Encomenda entregar(
            UUID id,
            String retiradoPor
    ) {

        Encomenda encomenda =
                buscarPorId(id);

        encomenda.entregar(retiradoPor);

        return encomendaRepository.save(encomenda);
    }
}