package br.com.condosys.api.repository;

import br.com.condosys.api.domain.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

// Responsável pelo acesso às unidades no banco.
public interface UnidadeRepository
        extends JpaRepository<Unidade, UUID> {

    // Verifica se já existe outra unidade com o mesmo bloco e número.
    boolean existsByBlocoAndNumero(
            String bloco,
            String numero
    );

    // Verifica duplicidade ignorando a própria unidade em uma atualização.
    boolean existsByBlocoAndNumeroAndIdNot(
            String bloco,
            String numero,
            UUID id
    );

    // Lista somente unidades ativas.
    List<Unidade> findAllByAtivaTrue();
}