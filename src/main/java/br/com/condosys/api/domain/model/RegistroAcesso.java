package br.com.condosys.api.domain.model;

import br.com.condosys.api.domain.enums.StatusAcesso;
import br.com.condosys.api.domain.enums.TipoMovimentoAcesso;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

// Representa uma entrada ou saída registrada no condomínio.
@Entity
@Table(name = "registros_acesso")
public class RegistroAcesso {

    // Identificador único do registro.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    // Pessoa que tentou acessar ou sair do condomínio.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "pessoa_id",
            nullable = false
    )
    private Pessoa pessoa;


    // Unidade relacionada ao acesso.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "unidade_id",
            nullable = false
    )
    private Unidade unidade;


    // Porteiro responsável pelo registro.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "porteiro_id",
            nullable = false
    )
    private Porteiro porteiro;


    // Autorização utilizada, quando existir.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autorizacao_id")
    private Autorizacao autorizacao;


    // Informa se foi uma entrada ou saída.
    @Enumerated(EnumType.STRING)
    @Column(
            name = "tipo_movimento",
            nullable = false,
            length = 20
    )
    private TipoMovimentoAcesso tipoMovimento;


    // Resultado da tentativa de acesso.
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private StatusAcesso status;


    // Indica se o porteiro liberou manualmente o acesso.
    @Column(
            name = "liberacao_manual",
            nullable = false
    )
    private boolean liberacaoManual;


    // Justificativa da liberação manual.
    @Column(name = "motivo_liberacao")
    private String motivoLiberacao;


    // Informação adicional sobre o registro.
    @Column(name = "observacao")
    private String observacao;


    // Momento da ocorrência.
    @Column(
            name = "data_hora",
            nullable = false
    )
    private OffsetDateTime dataHora;


    // Construtor obrigatório para o JPA.
    protected RegistroAcesso() {
    }


    // Cria um novo registro de acesso.
    public RegistroAcesso(
            Pessoa pessoa,
            Unidade unidade,
            Porteiro porteiro,
            Autorizacao autorizacao,
            TipoMovimentoAcesso tipoMovimento,
            StatusAcesso status,
            boolean liberacaoManual,
            String motivoLiberacao,
            String observacao
    ) {

        if (pessoa == null) {
            throw new IllegalArgumentException(
                    "A pessoa é obrigatória."
            );
        }

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

        if (tipoMovimento == null) {
            throw new IllegalArgumentException(
                    "O tipo de movimento é obrigatório."
            );
        }

        if (status == null) {
            throw new IllegalArgumentException(
                    "O status do acesso é obrigatório."
            );
        }

        // Liberação manual precisa de justificativa.
        if (liberacaoManual &&
                (motivoLiberacao == null ||
                        motivoLiberacao.isBlank())) {

            throw new IllegalArgumentException(
                    "A liberação manual exige uma justificativa."
            );
        }

        this.pessoa = pessoa;
        this.unidade = unidade;
        this.porteiro = porteiro;
        this.autorizacao = autorizacao;
        this.tipoMovimento = tipoMovimento;
        this.status = status;
        this.liberacaoManual = liberacaoManual;
        this.motivoLiberacao = motivoLiberacao;
        this.observacao = observacao;
        this.dataHora = OffsetDateTime.now();
    }


    public UUID getId() {
        return id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public Porteiro getPorteiro() {
        return porteiro;
    }

    public Autorizacao getAutorizacao() {
        return autorizacao;
    }

    public TipoMovimentoAcesso getTipoMovimento() {
        return tipoMovimento;
    }

    public StatusAcesso getStatus() {
        return status;
    }

    public boolean isLiberacaoManual() {
        return liberacaoManual;
    }

    public String getMotivoLiberacao() {
        return motivoLiberacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public OffsetDateTime getDataHora() {
        return dataHora;
    }
}