package br.com.condosys.api.repository;

import br.com.condosys.api.domain.model.AreaComum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

// Responsável pelo acesso às áreas comuns no banco.
public interface AreaComumRepository
        extends JpaRepository<AreaComum, UUID> {

    // Verifica se já existe uma área com o mesmo nome.
    boolean existsByNome(String nome);

    // Verifica duplicidade ignorando a própria área.
    boolean existsByNomeAndIdNot(
            String nome,
            UUID id
    );

    // Lista somente áreas ativas.
    List<AreaComum> findAllByAtivaTrue();
}