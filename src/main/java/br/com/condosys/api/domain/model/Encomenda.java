package br.com.condosys.api.domain.model;

import br.com.condosys.api.domain.enums.StatusEncomenda;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

// Representa uma encomenda recebida pela portaria.
@Entity
@Table(name = "encomendas")
public class Encomenda {

    // Identificador único da encomenda.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    // Unidade destinatária.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "unidade_id",
            nullable = false
    )
    private Unidade unidade;


    // Porteiro responsável pelo recebimento.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "porteiro_id",
            nullable = false
    )
    private Porteiro porteiro;


    // Empresa ou pessoa que enviou a encomenda.
    @Column(
            name = "remetente",
            length = 150
    )
    private String remetente;


    // Nome do destinatário.
    @Column(
            name = "destinatario",
            nullable = false,
            length = 150
    )
    private String destinatario;


    // Código de rastreamento, quando existir.
    @Column(
            name = "codigo_rastreio",
            length = 100
    )
    private String codigoRastreio;


    // Descrição opcional do volume.
    @Column(name = "descricao")
    private String descricao;


    // Momento em que a encomenda foi recebida.
    @Column(
            name = "recebido_em",
            nullable = false
    )
    private OffsetDateTime recebidoEm;


    // Momento em que foi retirada.
    @Column(name = "retirado_em")
    private OffsetDateTime retiradoEm;


    // Situação atual da encomenda.
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private StatusEncomenda status;


    // Pessoa que retirou a encomenda.
    @Column(
            name = "retirado_por",
            length = 150
    )
    private String retiradoPor;


    // Construtor obrigatório para o JPA.
    protected Encomenda() {
    }


    // Cria uma nova encomenda.
    public Encomenda(
            Unidade unidade,
            Porteiro porteiro,
            String remetente,
            String destinatario,
            String codigoRastreio,
            String descricao
    ) {

        if (unidade == null) {
            throw new IllegalArgumentException(
                    "A unidade é obrigatória."
            );
        }

        if (porteiro == null) {
            throw new IllegalArgumentException(
                    "O porteiro é obrigatório."
            );
        }

        if (destinatario == null ||
                destinatario.isBlank() ||
                destinatario.trim().length() > 150) {

            throw new IllegalArgumentException(
                    "Destinatário inválido."
            );
        }

        if (remetente != null &&
                remetente.length() > 150) {

            throw new IllegalArgumentException(
                    "O remetente deve ter no máximo 150 caracteres."
            );
        }

        if (codigoRastreio != null &&
                codigoRastreio.length() > 100) {

            throw new IllegalArgumentException(
                    "O código de rastreio deve ter no máximo 100 caracteres."
            );
        }

        this.unidade = unidade;
        this.porteiro = porteiro;
        this.remetente = remetente;
        this.destinatario = destinatario.trim();
        this.codigoRastreio = codigoRastreio;
        this.descricao = descricao;

        this.recebidoEm = OffsetDateTime.now();
        this.retiradoEm = null;

        this.status = StatusEncomenda.RECEBIDA;
        this.retiradoPor = null;
    }


    public UUID getId() {
        return id;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public Porteiro getPorteiro() {
        return porteiro;
    }

    public String getRemetente() {
        return remetente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getCodigoRastreio() {
        return codigoRastreio;
    }

    public String getDescricao() {
        return descricao;
    }

    public OffsetDateTime getRecebidoEm() {
        return recebidoEm;
    }

    public OffsetDateTime getRetiradoEm() {
        return retiradoEm;
    }

    public StatusEncomenda getStatus() {
        return status;
    }

    public String getRetiradoPor() {
        return retiradoPor;
    }


    // Indica que a encomenda já pode ser retirada.
    public void marcarAguardandoRetirada() {

        if (status == StatusEncomenda.ENTREGUE) {
            throw new IllegalStateException(
                    "Uma encomenda entregue não pode voltar para retirada."
            );
        }

        this.status = StatusEncomenda.AGUARDANDO_RETIRADA;
    }


    // Registra a entrega da encomenda.
    public void entregar(String retiradoPor) {

        if (status == StatusEncomenda.ENTREGUE) {
            throw new IllegalStateException(
                    "A encomenda já foi entregue."
            );
        }

        if (retiradoPor == null ||
                retiradoPor.isBlank() ||
                retiradoPor.trim().length() > 150) {

            throw new IllegalArgumentException(
                    "Informe quem retirou a encomenda."
            );
        }

        this.retiradoPor = retiradoPor.trim();
        this.retiradoEm = OffsetDateTime.now();
        this.status = StatusEncomenda.ENTREGUE;
    }
}