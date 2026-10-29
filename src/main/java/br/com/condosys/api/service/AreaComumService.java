package br.com.condosys.api.service;

import br.com.condosys.api.domain.model.AreaComum;
import br.com.condosys.api.exception.AreaComumNaoEncontradaException;
import br.com.condosys.api.repository.AreaComumRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

// Regras de negócio relacionadas às áreas comuns.
@Service
public class AreaComumService {

    private final AreaComumRepository areaComumRepository;

    public AreaComumService(
            AreaComumRepository areaComumRepository
    ) {
        this.areaComumRepository = areaComumRepository;
    }

    // Cadastra uma nova área comum.
    @Transactional
    public AreaComum cadastrar(
            String nome,
            Integer capacidade,
            String descricao
    ) {

        if (areaComumRepository.existsByNome(nome)) {
            throw new IllegalStateException(
                    "Já existe uma área comum com este nome."
            );
        }

        AreaComum areaComum =
                new AreaComum(
                        nome,
                        capacidade,
                        descricao
                );

        return areaComumRepository.save(areaComum);
    }

    // Lista somente áreas ativas.
    @Transactional(readOnly = true)
    public List<AreaComum> listarTodas() {

        return areaComumRepository.findAllByAtivaTrue();
    }

    // Busca área pelo UUID.
    @Transactional(readOnly = true)
    public AreaComum buscarPorId(UUID id) {

        return areaComumRepository
                .findById(id)
                .orElseThrow(
                        AreaComumNaoEncontradaException::new
                );
    }

    // Atualiza os dados da área.
    @Transactional
    public AreaComum atualizar(
            UUID id,
            String nome,
            Integer capacidade,
            String descricao
    ) {

        AreaComum areaComum =
                buscarPorId(id);

        if (areaComumRepository
                .existsByNomeAndIdNot(nome, id)) {

            throw new IllegalStateException(
                    "Já existe outra área comum com este nome."
            );
        }

        areaComum.atualizar(
                nome,
                capacidade,
                descricao
        );

        return areaComumRepository.save(areaComum);
    }

    // Desativa a área.
    @Transactional
    public void desativar(UUID id) {

        AreaComum areaComum =
                buscarPorId(id);

        areaComum.desativar();

        areaComumRepository.save(areaComum);
    }

    // Reativa a área.
    @Transactional
    public AreaComum reativar(UUID id) {

        AreaComum areaComum =
                buscarPorId(id);

        areaComum.reativar();

        return areaComumRepository.save(areaComum);
    }
}