package br.com.condosys.api.repository;

import br.com.condosys.api.domain.model.Proprietario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// Responsável pelo acesso aos proprietários no banco.
public interface ProprietarioRepository
        extends JpaRepository<Proprietario, UUID> {
}