package br.com.condosys.api.repository;

import br.com.condosys.api.domain.model.Porteiro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// Responsável pelo acesso aos porteiros no banco.
public interface PorteiroRepository
        extends JpaRepository<Porteiro, UUID> {
}