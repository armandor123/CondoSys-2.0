package br.com.condosys.api.domain.model;

import jakarta.persistence.*;

import java.util.UUID;

// Representa uma área comum disponível no condomínio.
@Entity
@Table(name = "areas_comuns")
public class AreaComum {

    // Identificador único da área comum.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Nome da área comum.
    @Column(
            name = "nome",
            nullable = false,
            unique = true,
            length = 100
    )
    private String nome;

    // Quantidade máxima de pessoas.
    @Column(
            name = "capacidade",
            nullable = false
    )
    private Integer capacidade;

    // Indica se a área está disponível no sistema.
    @Column(
            name = "ativa",
            nullable = false
    )
    private boolean ativa = true;

    // Informação adicional sobre o espaço.
    @Column(name = "descricao")
    private String descricao;

    // Construtor obrigatório para o JPA.
    protected AreaComum() {
    }

    // Cria uma nova área comum.
    public AreaComum(
            String nome,
            Integer capacidade,
            String descricao
    ) {

        if (nome == null ||
                nome.isBlank() ||
                nome.trim().length() > 100) {

            throw new IllegalArgumentException(
                    "Nome da área comum inválido."
            );
        }

        if (capacidade == null ||
                capacidade < 0) {

            throw new IllegalArgumentException(
                    "Capacidade inválida."
            );
        }

        this.nome = nome.trim();
        this.capacidade = capacidade;
        this.descricao = descricao;
        this.ativa = true;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public String getDescricao() {
        return descricao;
    }

    // Atualiza os dados da área comum.
    public void atualizar(
            String nome,
            Integer capacidade,
            String descricao
    ) {

        if (nome == null ||
                nome.isBlank() ||
                nome.trim().length() > 100) {

            throw new IllegalArgumentException(
                    "Nome da área comum inválido."
            );
        }

        if (capacidade == null ||
                capacidade < 0) {

            throw new IllegalArgumentException(
                    "Capacidade inválida."
            );
        }

        this.nome = nome.trim();
        this.capacidade = capacidade;
        this.descricao = descricao;
    }

    // Desativa a área sem apagar o registro.
    public void desativar() {
        this.ativa = false;
    }

    // Reativa a área.
    public void reativar() {
        this.ativa = true;
    }
}