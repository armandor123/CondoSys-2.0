package br.com.condosys.api.domain.model;

import br.com.condosys.api.domain.enums.PerfilUsuario;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

// Representa um proprietário do condomínio.
@Entity
@Table(name = "proprietarios")
public class Proprietario {

    // Compartilha o mesmo UUID do Usuario.
    @Id
    @Column(name = "usuario_id")
    private UUID usuarioId;

    // Relaciona o proprietário ao usuário cadastrado.
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Data em que o proprietário aderiu ao sistema.
    @Column(name = "data_adesao", nullable = false)
    private LocalDate dataAdesao;

    // Construtor obrigatório para o JPA.
    protected Proprietario() {
    }

    // Cria um proprietário associado a um usuário.
    public Proprietario(
            Usuario usuario,
            LocalDate dataAdesao
    ) {

        // Exige usuário com perfil PROPRIETARIO.
        if (usuario == null ||
                usuario.getPerfil() != PerfilUsuario.PROPRIETARIO) {

            throw new IllegalArgumentException(
                    "O usuário deve possuir perfil PROPRIETARIO."
            );
        }

        // Valida a data de adesão.
        if (dataAdesao == null ||
                dataAdesao.isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Data de adesão inválida."
            );
        }

        this.usuario = usuario;
        this.dataAdesao = dataAdesao;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getDataAdesao() {
        return dataAdesao;
    }
}