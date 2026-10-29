package br.com.condosys.api.repository;

import br.com.condosys.api.domain.enums.StatusReserva;
import br.com.condosys.api.domain.model.Reserva;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

// Responsável pelo acesso às reservas no banco.
public interface ReservaRepository
        extends JpaRepository<Reserva, UUID> {

    // Verifica conflito de horário para uma área.
    boolean existsByAreaComum_IdAndStatusAndInicioLessThanAndFimGreaterThan(
            UUID areaComumId,
            StatusReserva status,
            OffsetDateTime fim,
            OffsetDateTime inicio
    );

    // Lista reservas de uma unidade.
    List<Reserva> findAllByUnidade_IdOrderByInicioDesc(
            UUID unidadeId
    );

    // Lista reservas de uma área comum.
    List<Reserva> findAllByAreaComum_IdOrderByInicioDesc(
            UUID areaComumId
    );

    // Lista reservas por status.
    List<Reserva> findAllByStatusOrderByInicioDesc(
            StatusReserva status
    );
}