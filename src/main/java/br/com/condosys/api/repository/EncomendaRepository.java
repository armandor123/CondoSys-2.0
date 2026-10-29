package br.com.condosys.api.repository;

import br.com.condosys.api.domain.enums.StatusEncomenda;
import br.com.condosys.api.domain.model.Encomenda;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

// Responsável pelo acesso às encomendas no banco.
public interface EncomendaRepository
        extends JpaRepository<Encomenda, UUID> {

    // Lista encomendas de uma unidade, das mais recentes para as mais antigas.
    List<Encomenda> findAllByUnidade_IdOrderByRecebidoEmDesc(
            UUID unidadeId
    );

    // Lista encomendas conforme o status.
    List<Encomenda> findAllByStatusOrderByRecebidoEmDesc(
            StatusEncomenda status
    );
}