package br.com.condosys.api.domain.model;

import jakarta.persistence.*;

import java.util.UUID;

// Representa uma unidade do condomínio.
@Entity
@Table(
        name = "unidades",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_unidades_bloco_numero",
                        columnNames = {"bloco", "numero"}
                )
        }
)
public class Unidade {

    // Identificador único da unidade.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Proprietário responsável pela unidade.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "proprietario_id",
            nullable = false
    )
    private Proprietario proprietario;

    // Bloco ou torre da unidade.
    @Column(
            name = "bloco",
            nullable = false,
            length = 20
    )
    private String bloco;

    // Número da unidade.
    @Column(
            name = "numero",
            nullable = false,
            length = 20
    )
    private String numero;

    // Andar da unidade.
    @Column(name = "andar")
    private Integer andar;

    // Indica se a unidade está ativa.
    @Column(
            name = "ativa",
            nullable = false
    )
    private boolean ativa = true;

    // Construtor obrigatório para o JPA.
    protected Unidade() {
    }

    // Cria uma nova unidade.
    public Unidade(
            Proprietario proprietario,
            String bloco,
            String numero,
            Integer andar
    ) {

        // Exige um proprietário válido.
        if (proprietario == null) {
            throw new IllegalArgumentException(
                    "O proprietário é obrigatório."
            );
        }

        // Valida o bloco.
        if (bloco == null ||
                bloco.isBlank() ||
                bloco.trim().length() > 20) {

            throw new IllegalArgumentException(
                    "Bloco inválido."
            );
        }

        // Valida o número.
        if (numero == null ||
                numero.isBlank() ||
                numero.trim().length() > 20) {

            throw new IllegalArgumentException(
                    "Número da unidade inválido."
            );
        }

        this.proprietario = proprietario;
        this.bloco = bloco.trim();
        this.numero = numero.trim();
        this.andar = andar;
        this.ativa = true;
    }

    public UUID getId() {
        return id;
    }

    public Proprietario getProprietario() {
        return proprietario;
    }

    public String getBloco() {
        return bloco;
    }

    public String getNumero() {
        return numero;
    }

    public Integer getAndar() {
        return andar;
    }

    public boolean isAtiva() {
        return ativa;
    }

    // Atualiza os dados básicos da unidade.
    public void atualizar(
            String bloco,
            String numero,
            Integer andar
    ) {

        if (bloco == null ||
                bloco.isBlank() ||
                bloco.trim().length() > 20) {

            throw new IllegalArgumentException(
                    "Bloco inválido."
            );
        }

        if (numero == null ||
                numero.isBlank() ||
                numero.trim().length() > 20) {

            throw new IllegalArgumentException(
                    "Número da unidade inválido."
            );
        }

        this.bloco = bloco.trim();
        this.numero = numero.trim();
        this.andar = andar;
    }

    // Desativa a unidade sem apagar o registro.
    public void desativar() {
        this.ativa = false;
    }

    // Reativa a unidade.
    public void reativar() {
        this.ativa = true;
    }
}