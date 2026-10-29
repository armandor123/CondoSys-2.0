package br.com.condosys.api.service;

import br.com.condosys.api.domain.model.Proprietario;
import br.com.condosys.api.domain.model.Unidade;
import br.com.condosys.api.exception.UnidadeNaoEncontradaException;
import br.com.condosys.api.repository.UnidadeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

// Regras de negócio relacionadas às unidades.
@Service
public class UnidadeService {

    private final UnidadeRepository unidadeRepository;
    private final ProprietarioService proprietarioService;

    // Spring injeta as dependências.
    public UnidadeService(
            UnidadeRepository unidadeRepository,
            ProprietarioService proprietarioService
    ) {
        this.unidadeRepository = unidadeRepository;
        this.proprietarioService = proprietarioService;
    }


    // Cadastra uma nova unidade.
    @Transactional
    public Unidade cadastrar(
            UUID proprietarioId,
            String bloco,
            String numero,
            Integer andar
    ) {

        // Confirma que o proprietário existe.
        Proprietario proprietario =
                proprietarioService.buscarPorId(proprietarioId);

        // Impede unidade duplicada.
        if (unidadeRepository.existsByBlocoAndNumero(
                bloco,
                numero
        )) {
            throw new IllegalStateException(
                    "Já existe uma unidade com este bloco e número."
            );
        }

        Unidade unidade = new Unidade(
                proprietario,
                bloco,
                numero,
                andar
        );

        return unidadeRepository.save(unidade);
    }


    // Lista somente unidades ativas.
    @Transactional(readOnly = true)
    public List<Unidade> listarTodas() {

        return unidadeRepository.findAllByAtivaTrue();
    }


    // Busca unidade pelo UUID.
    @Transactional(readOnly = true)
    public Unidade buscarPorId(UUID id) {

        return unidadeRepository
                .findById(id)
                .orElseThrow(
                        UnidadeNaoEncontradaException::new
                );
    }


    // Atualiza os dados da unidade.
    @Transactional
    public Unidade atualizar(
            UUID id,
            String bloco,
            String numero,
            Integer andar
    ) {

        Unidade unidade = buscarPorId(id);

        // Impede duplicidade com outra unidade.
        if (unidadeRepository
                .existsByBlocoAndNumeroAndIdNot(
                        bloco,
                        numero,
                        id
                )) {

            throw new IllegalStateException(
                    "Já existe outra unidade com este bloco e número."
            );
        }

        unidade.atualizar(
                bloco,
                numero,
                andar
        );

        return unidadeRepository.save(unidade);
    }


    // Desativa a unidade sem apagar o registro.
    @Transactional
    public void desativar(UUID id) {

        Unidade unidade = buscarPorId(id);

        unidade.desativar();

        unidadeRepository.save(unidade);
    }


    // Reativa uma unidade desativada.
    @Transactional
    public Unidade reativar(UUID id) {

        Unidade unidade = buscarPorId(id);

        unidade.reativar();

        return unidadeRepository.save(unidade);
    }
}