package br.com.condosys.api.repository;

import br.com.condosys.api.domain.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// Responsável pelo acesso aos administradores no banco.
public interface AdministradorRepository
        extends JpaRepository<Administrador, UUID> {

}