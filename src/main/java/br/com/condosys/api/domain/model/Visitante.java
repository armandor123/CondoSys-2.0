package br.com.condosys.api.domain.model;

import br.com.condosys.api.domain.enums.TipoPessoa;
import jakarta.persistence.*;

import java.util.UUID;

// Representa um visitante do condomínio.
@Entity
@Table(name = "visitantes")
public class Visitante {

    // Compartilha o mesmo UUID da Pessoa.
    @Id
    @Column(name = "pessoa_id")
    private UUID pessoaId;

    // Relaciona o visitante ao cadastro base de Pessoa.
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    // Observação opcional sobre o visitante.
    @Column(name = "observacao")
    private String observacao;

    // Construtor obrigatório para o JPA.
    protected Visitante() {
    }

    // Cria um visitante associado a uma Pessoa.
    public Visitante(
            Pessoa pessoa,
            String observacao
    ) {

        // Exige pessoa do tipo VISITANTE.
        if (pessoa == null ||
                pessoa.getTipoPessoa() != TipoPessoa.VISITANTE) {

            throw new IllegalArgumentException(
                    "A pessoa deve possuir tipo VISITANTE."
            );
        }

        this.pessoa = pessoa;
        this.observacao = observacao;
    }

    public UUID getPessoaId() {
        return pessoaId;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public String getObservacao() {
        return observacao;
    }

    // Atualiza a observação do visitante.
    public void atualizarObservacao(String observacao) {
        this.observacao = observacao;
    }
}