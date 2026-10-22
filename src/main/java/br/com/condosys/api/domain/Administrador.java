package br.com.condosys.api.domain.model;

import br.com.condosys.api.domain.enums.PerfilUsuario;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

// Dados específicos de um usuário administrador.
@Entity
@Table(name = "administradores")
public class Administrador {

    @Id
    @Column(name = "usuario_id")
    private UUID usuarioId;

    // Compartilha a chave primária com Usuario.
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Data de contratação do administrador.
    @Column(name = "data_contratacao", nullable = false)
    private LocalDate dataContratacao;

    // Construtor exigido pelo JPA.
    protected Administrador() {
    }

    // Cria um administrador associado a um usuário.
    public Administrador(Usuario usuario, LocalDate dataContratacao) {

        if (usuario == null ||
                usuario.getPerfil() != PerfilUsuario.ADMINISTRADOR) {
            throw new IllegalArgumentException(
                    "O usuário deve possuir perfil ADMINISTRADOR."
            );
        }

        this.usuario = usuario;
        this.dataContratacao = dataContratacao;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getDataContratacao() {
        return dataContratacao;
    }
}