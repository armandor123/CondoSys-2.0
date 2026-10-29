package br.com.condosys.api.service;

import br.com.condosys.api.domain.enums.StatusReserva;

import br.com.condosys.api.domain.model.AreaComum;
import br.com.condosys.api.domain.model.Reserva;
import br.com.condosys.api.domain.model.Unidade;
import br.com.condosys.api.domain.model.Usuario;

import br.com.condosys.api.exception.ReservaNaoEncontradaException;
import br.com.condosys.api.repository.ReservaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

// Regras de negócio relacionadas às reservas.
@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UnidadeService unidadeService;
    private final AreaComumService areaComumService;
    private final UsuarioService usuarioService;

    public ReservaService(
            ReservaRepository reservaRepository,
            UnidadeService unidadeService,
            AreaComumService areaComumService,
            UsuarioService usuarioService
    ) {
        this.reservaRepository = reservaRepository;
        this.unidadeService = unidadeService;
        this.areaComumService = areaComumService;
        this.usuarioService = usuarioService;
    }

    // Cria uma nova reserva.
    @Transactional
    public Reserva cadastrar(
            UUID unidadeId,
            UUID areaComumId,
            UUID usuarioId,
            OffsetDateTime inicio,
            OffsetDateTime fim,
            String observacao
    ) {

        Unidade unidade =
                unidadeService.buscarPorId(unidadeId);

        AreaComum areaComum =
                areaComumService.buscarPorId(areaComumId);

        Usuario usuario =
                usuarioService.buscarPorId(usuarioId);

        if (!unidade.isAtiva()) {
            throw new IllegalArgumentException(
                    "A unidade está inativa."
            );
        }

        if (!areaComum.isAtiva()) {
            throw new IllegalArgumentException(
                    "A área comum está inativa."
            );
        }

        if (!usuario.isAtivo()) {
            throw new IllegalArgumentException(
                    "O usuário está inativo."
            );
        }

        if (inicio == null ||
                fim == null ||
                !fim.isAfter(inicio)) {

            throw new IllegalArgumentException(
                    "Período da reserva inválido."
            );
        }

        // Impede duas reservas ativas no mesmo período.
        if (reservaRepository
                .existsByAreaComum_IdAndStatusAndInicioLessThanAndFimGreaterThan(
                        areaComumId,
                        StatusReserva.ATIVA,
                        fim,
                        inicio
                )) {

            throw new IllegalStateException(
                    "A área comum já possui reserva neste período."
            );
        }

        Reserva reserva =
                new Reserva(
                        unidade,
                        areaComum,
                        usuario,
                        inicio,
                        fim,
                        observacao
                );

        return reservaRepository.save(reserva);
    }

    // Lista todas as reservas.
    @Transactional(readOnly = true)
    public List<Reserva> listarTodas() {

        return reservaRepository.findAll();
    }

    // Busca uma reserva pelo UUID.
    @Transactional(readOnly = true)
    public Reserva buscarPorId(UUID id) {

        return reservaRepository
                .findById(id)
                .orElseThrow(
                        ReservaNaoEncontradaException::new
                );
    }

    // Lista reservas de uma unidade.
    @Transactional(readOnly = true)
    public List<Reserva> listarPorUnidade(
            UUID unidadeId
    ) {

        unidadeService.buscarPorId(unidadeId);

        return reservaRepository
                .findAllByUnidade_IdOrderByInicioDesc(
                        unidadeId
                );
    }

    // Lista reservas de uma área.
    @Transactional(readOnly = true)
    public List<Reserva> listarPorArea(
            UUID areaComumId
    ) {

        areaComumService.buscarPorId(areaComumId);

        return reservaRepository
                .findAllByAreaComum_IdOrderByInicioDesc(
                        areaComumId
                );
    }

    // Lista por status.
    @Transactional(readOnly = true)
    public List<Reserva> listarPorStatus(
            StatusReserva status
    ) {

        return reservaRepository
                .findAllByStatusOrderByInicioDesc(status);
    }

    // Cancela uma reserva.
    @Transactional
    public Reserva cancelar(UUID id) {

        Reserva reserva =
                buscarPorId(id);

        reserva.cancelar();

        return reservaRepository.save(reserva);
    }

    // Conclui uma reserva.
    @Transactional
    public Reserva concluir(UUID id) {

        Reserva reserva =
                buscarPorId(id);

        reserva.concluir();

        return reservaRepository.save(reserva);
    }
}