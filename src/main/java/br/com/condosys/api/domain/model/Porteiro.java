
package br.com.condosys.api.domain.model;

import br.com.condosys.api.domain.enums.PerfilUsuario;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

// Representa um porteiro do condomínio.
@Entity
@Table(name = "porteiros")
public class Porteiro {

    // Compartilha o identificador com Usuario.
    @Id
    @Column(name = "usuario_id")
    private UUID usuarioId;

    // Relaciona o porteiro ao usuário cadastrado.
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Turno de trabalho do porteiro.
    @Column(name = "turno", nullable = false, length = 30)
    private String turno;

    // Permanece nula enquanto não houver demissão.
    @Column(name = "data_demissao")
    private LocalDate dataDemissao;

    // Construtor obrigatório para o JPA.
    protected Porteiro() {
    }

    // Cria um porteiro associado a um usuário.
    public Porteiro(Usuario usuario, String turno) {

        // Exige um usuário com perfil PORTEIRO.
        if (usuario == null ||
                usuario.getPerfil() != PerfilUsuario.PORTEIRO) {

            throw new IllegalArgumentException(
                    "O usuário deve possuir perfil PORTEIRO."
            );
        }

        // Valida o turno informado.
        if (turno == null ||
                turno.isBlank() ||
                turno.trim().length() > 30) {

            throw new IllegalArgumentException(
                    "O turno deve conter entre 1 e 30 caracteres."
            );
        }

        this.usuario = usuario;
        this.turno = turno.trim();
        this.dataDemissao = null;
    }

    // Retorna o identificador compartilhado.
    public UUID getUsuarioId() {
        return usuarioId;
    }

    // Retorna o usuário relacionado.
    public Usuario getUsuario() {
        return usuario;
    }

    // Retorna o turno de trabalho.
    public String getTurno() {
        return turno;
    }

    // Retorna a data de demissão.
    public LocalDate getDataDemissao() {
        return dataDemissao;
    }

    // Permite atualizar o turno de trabalho.
    public void atualizarTurno(String novoTurno) {

        if (novoTurno == null ||
                novoTurno.isBlank() ||
                novoTurno.trim().length() > 30) {

            throw new IllegalArgumentException(
                    "Turno inválido."
            );
        }

        this.turno = novoTurno.trim();
    }

    // Registra a demissão do porteiro.
    public void registrarDemissao(LocalDate dataDemissao) {

        if (dataDemissao == null ||
                dataDemissao.isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Data de demissão inválida."
            );
        }

        if (this.dataDemissao != null) {
            throw new IllegalStateException(
                    "A demissão já foi registrada."
            );
        }

        this.dataDemissao = dataDemissao;
    }
}
