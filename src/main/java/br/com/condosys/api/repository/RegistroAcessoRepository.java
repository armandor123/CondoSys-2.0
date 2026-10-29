package br.com.condosys.api.repository;

import br.com.condosys.api.domain.model.RegistroAcesso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

// Responsável pelo acesso aos registros de entrada e saída.
public interface RegistroAcessoRepository
        extends JpaRepository<RegistroAcesso, UUID> {

    // Lista os acessos de uma unidade do mais recente para o mais antigo.
    List<RegistroAcesso> findAllByUnidade_IdOrderByDataHoraDesc(
            UUID unidadeId
    );

    // Lista os acessos de uma pessoa.
    List<RegistroAcesso> findAllByPessoa_IdOrderByDataHoraDesc(
            UUID pessoaId
    );
}