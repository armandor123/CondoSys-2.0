package br.com.condosys.api.domain.model;

import br.com.condosys.api.domain.enums.StatusReserva;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

// Representa uma reserva de área comum.
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Unidade responsável pela reserva.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "unidade_id",
            nullable = false
    )
    private Unidade unidade;

    // Área comum reservada.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "area_comum_id",
            nullable = false
    )
    private AreaComum areaComum;

    // Usuário que realizou a reserva.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "criado_por_usuario_id",
            nullable = false
    )
    private Usuario criadoPorUsuario;

    // Início da reserva.
    @Column(
            name = "inicio",
            nullable = false
    )
    private OffsetDateTime inicio;

    // Final da reserva.
    @Column(
            name = "fim",
            nullable = false
    )
    private OffsetDateTime fim;

    // Situação da reserva.
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private StatusReserva status;

    // Informação adicional.
    @Column(name = "observacao")
    private String observacao;

    // Data de criação.
    @Column(
            name = "criado_em",
            nullable = false
    )
    private OffsetDateTime criadoEm;

    protected Reserva() {
    }

    // Cria uma nova reserva.
    public Reserva(
            Unidade unidade,
            AreaComum areaComum,
            Usuario criadoPorUsuario,
            OffsetDateTime inicio,
            OffsetDateTime fim,
            String observacao
    ) {

        if (unidade == null) {
            throw new IllegalArgumentException(
                    "A unidade é obrigatória."
            );
        }

        if (areaComum == null) {
            throw new IllegalArgumentException(
                    "A área comum é obrigatória."
            );
        }

        if (criadoPorUsuario == null) {
            throw new IllegalArgumentException(
                    "O usuário responsável é obrigatório."
            );
        }

        if (inicio == null || fim == null) {
            throw new IllegalArgumentException(
                    "O início e o fim da reserva são obrigatórios."
            );
        }

        if (!fim.isAfter(inicio)) {
            throw new IllegalArgumentException(
                    "O fim da reserva deve ser posterior ao início."
            );
        }

        this.unidade = unidade;
        this.areaComum = areaComum;
        this.criadoPorUsuario = criadoPorUsuario;
        this.inicio = inicio;
        this.fim = fim;
        this.observacao = observacao;

        this.status = StatusReserva.ATIVA;
        this.criadoEm = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public AreaComum getAreaComum() {
        return areaComum;
    }

    public Usuario getCriadoPorUsuario() {
        return criadoPorUsuario;
    }

    public OffsetDateTime getInicio() {
        return inicio;
    }

    public OffsetDateTime getFim() {
        return fim;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public String getObservacao() {
        return observacao;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    // Cancela uma reserva ativa.
    public void cancelar() {

        if (status != StatusReserva.ATIVA) {
            throw new IllegalStateException(
                    "Somente reservas ativas podem ser canceladas."
            );
        }

        this.status = StatusReserva.CANCELADA;
    }

    // Marca a reserva como concluída.
    public void concluir() {

        if (status != StatusReserva.ATIVA) {
            throw new IllegalStateException(
                    "Somente reservas ativas podem ser concluídas."
            );
        }

        this.status = StatusReserva.CONCLUIDA;
    }
}