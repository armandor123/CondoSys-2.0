package br.com.condosys.api.repository;

import br.com.condosys.api.domain.enums.StatusAutorizacao;
import br.com.condosys.api.domain.model.Autorizacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

// Responsável pelo acesso às autorizações no banco.
public interface AutorizacaoRepository
        extends JpaRepository<Autorizacao, UUID> {

    // Lista autorizações por unidade e status.
    List<Autorizacao> findAllByUnidade_IdAndStatus(
            UUID unidadeId,
            StatusAutorizacao status
    );
}