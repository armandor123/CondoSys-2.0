package br.com.condosys.api.repository;

import br.com.condosys.api.domain.model.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// Responsável pelo acesso aos visitantes no banco.
public interface VisitanteRepository
        extends JpaRepository<Visitante, UUID> {
}