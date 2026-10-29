package br.com.condosys.api.domain.model;

import br.com.condosys.api.domain.enums.StatusAutorizacao;
import br.com.condosys.api.domain.enums.TipoAutorizacao;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

// Representa uma autorização de acesso ao condomínio.
@Entity
@Table(name = "autorizacoes")
public class Autorizacao {

    // Identificador único da autorização.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    // Pessoa autorizada a acessar o condomínio.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "pessoa_id",
            nullable = false
    )
    private Pessoa pessoa;


    // Proprietário responsável pela autorização.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "proprietario_id",
            nullable = false
    )
    private Proprietario proprietario;


    // Unidade relacionada à autorização.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "unidade_id",
            nullable = false
    )
    private Unidade unidade;


    // Tipo da autorização.
    @Enumerated(EnumType.STRING)
    @Column(
            name = "tipo",
            nullable = false,
            length = 30
    )
    private TipoAutorizacao tipo;


    // Situação atual da autorização.
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private StatusAutorizacao status;


    // Momento a partir do qual o acesso é permitido.
    @Column(
            name = "inicio_validade",
            nullable = false
    )
    private OffsetDateTime inicioValidade;


    // Momento final da autorização.
    @Column(name = "fim_validade")
    private OffsetDateTime fimValidade;


    // Informação adicional sobre a autorização.
    @Column(name = "observacao")
    private String observacao;


    // Momento em que a autorização foi criada.
    @Column(
            name = "criado_em",
            nullable = false
    )
    private OffsetDateTime criadoEm;


    // Momento em que foi revogada.
    @Column(name = "revogado_em")
    private OffsetDateTime revogadoEm;


    // Construtor obrigatório para o JPA.
    protected Autorizacao() {
    }


    // Cria uma nova autorização.
    public Autorizacao(
            Pessoa pessoa,
            Proprietario proprietario,
            Unidade unidade,
            TipoAutorizacao tipo,
            OffsetDateTime inicioValidade,
            OffsetDateTime fimValidade,
            String observacao
    ) {

        if (pessoa == null) {
            throw new IllegalArgumentException(
                    "A pessoa é obrigatória."
            );
        }

        if (proprietario == null) {
            throw new IllegalArgumentException(
                    "O proprietário é obrigatório."
            );
        }

        if (unidade == null) {
            throw new IllegalArgumentException(
                    "A unidade é obrigatória."
            );
        }

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "O tipo da autorização é obrigatório."
            );
        }

        if (inicioValidade == null) {
            throw new IllegalArgumentException(
                    "O início da validade é obrigatório."
            );
        }


        // Prazo determinado exige uma data final.
        if (tipo == TipoAutorizacao.PRAZO_DETERMINADO &&
                fimValidade == null) {

            throw new IllegalArgumentException(
                    "Autorização por prazo determinado exige data final."
            );
        }


        // Prazo indeterminado não deve possuir data final.
        if (tipo == TipoAutorizacao.PRAZO_INDETERMINADO &&
                fimValidade != null) {

            throw new IllegalArgumentException(
                    "Autorização por prazo indeterminado não deve possuir data final."
            );
        }


        // Caso exista fim, ele precisa ser posterior ao início.
        if (fimValidade != null &&
                !fimValidade.isAfter(inicioValidade)) {

            throw new IllegalArgumentException(
                    "O fim da validade deve ser posterior ao início."
            );
        }

        this.pessoa = pessoa;
        this.proprietario = proprietario;
        this.unidade = unidade;
        this.tipo = tipo;
        this.status = StatusAutorizacao.ATIVA;
        this.inicioValidade = inicioValidade;
        this.fimValidade = fimValidade;
        this.observacao = observacao;
        this.criadoEm = OffsetDateTime.now();
        this.revogadoEm = null;
    }


    public UUID getId() {
        return id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public Proprietario getProprietario() {
        return proprietario;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public TipoAutorizacao getTipo() {
        return tipo;
    }

    public StatusAutorizacao getStatus() {
        return status;
    }

    public OffsetDateTime getInicioValidade() {
        return inicioValidade;
    }

    public OffsetDateTime getFimValidade() {
        return fimValidade;
    }

    public String getObservacao() {
        return observacao;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public OffsetDateTime getRevogadoEm() {
        return revogadoEm;
    }


    // Verifica se a autorização pode ser utilizada agora.
    public boolean estaValida() {

        OffsetDateTime agora = OffsetDateTime.now();

        if (status != StatusAutorizacao.ATIVA) {
            return false;
        }

        if (agora.isBefore(inicioValidade)) {
            return false;
        }

        return fimValidade == null ||
                agora.isBefore(fimValidade);
    }


    // Revoga uma autorização ativa.
    public void revogar() {

        if (status != StatusAutorizacao.ATIVA) {
            throw new IllegalStateException(
                    "Somente autorizações ativas podem ser revogadas."
            );
        }

        this.status = StatusAutorizacao.REVOGADA;
        this.revogadoEm = OffsetDateTime.now();
    }


    // Marca uma autorização de entrada única como utilizada.
    public void marcarComoUtilizada() {

        if (tipo != TipoAutorizacao.UNICA_ENTRADA) {
            throw new IllegalStateException(
                    "Somente autorização de entrada única pode ser marcada como utilizada."
            );
        }

        if (status != StatusAutorizacao.ATIVA) {
            throw new IllegalStateException(
                    "A autorização não está ativa."
            );
        }

        this.status = StatusAutorizacao.UTILIZADA;
    }


    // Marca uma autorização como expirada.
    public void expirar() {

        if (status == StatusAutorizacao.ATIVA) {
            this.status = StatusAutorizacao.EXPIRADA;
        }
    }
}