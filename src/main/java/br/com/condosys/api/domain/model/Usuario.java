package br.com.condosys.api.domain.model;

import br.com.condosys.api.domain.enums.PerfilUsuario;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;


// Representa um usuário que pode acessar o CondoSys.
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    // Cada usuário está ligado a uma única Pessoa.
    @OneToOne
    @JoinColumn(
            name = "pessoa_id",
            nullable = false,
            unique = true
    )
    private Pessoa pessoa;


    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;


    // Guarda somente a senha criptografada/hash, nunca senha em texto.
    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;


    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false, length = 30)
    private PerfilUsuario perfil;


    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;


    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;


    @Column(name = "atualizado_em", nullable = false)
    private OffsetDateTime atualizadoEm;

    // Construtor exigido pelo JPA/Hibernate.
    protected Usuario() {
    }


    // Cria um novo usuário do CondoSys.
    public Usuario(
            Pessoa pessoa,
            String email,
            String senhaHash,
            PerfilUsuario perfil
    ) {
        this.pessoa = pessoa;
        this.email = email;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
    }


    // Preenche as datas antes do primeiro INSERT.
    @PrePersist
    private void prePersist() {
        OffsetDateTime agora = OffsetDateTime.now();

        if (criadoEm == null) {
            criadoEm = agora;
        }

        atualizadoEm = agora;
    }


    // Atualiza a data antes de um UPDATE.
    @PreUpdate
    private void preUpdate() {
        atualizadoEm = OffsetDateTime.now();
    }

    // =========================================================
// GETTERS
// =========================================================

    public UUID getId() {
        return id;
    }


    public Pessoa getPessoa() {
        return pessoa;
    }


    public String getEmail() {
        return email;
    }


    public PerfilUsuario getPerfil() {
        return perfil;
    }


    public boolean isAtivo() {
        return ativo;
    }


    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }


    public OffsetDateTime getAtualizadoEm() {
        return atualizadoEm;
    }


// =========================================================
// COMPORTAMENTOS
// =========================================================

    // Altera o e-mail do usuário.
    public void alterarEmail(String email) {
        this.email = email;
    }


    // Altera o perfil de acesso.
    public void alterarPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }


    // Atualiza o hash da senha.
    public void alterarSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }


    // Ativa o acesso ao sistema.
    public void ativar() {
        this.ativo = true;
    }


    // Desativa o acesso sem apagar o usuário.
    public void desativar() {
        this.ativo = false;
    }

    // Permite validar a senha internamente.
// O hash não será exposto pelos DTOs da API.
    public String getSenhaHash() {
        return senhaHash;
    }
}