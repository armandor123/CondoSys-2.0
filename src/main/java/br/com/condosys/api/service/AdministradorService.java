package br.com.condosys.api.service;

import br.com.condosys.api.domain.enums.PerfilUsuario;
import br.com.condosys.api.domain.model.Administrador;
import br.com.condosys.api.domain.model.Usuario;
import br.com.condosys.api.repository.AdministradorRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

// Regras de negócio dos administradores.
@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final UsuarioService usuarioService;

    // Spring injeta as dependências.
    public AdministradorService(
            AdministradorRepository administradorRepository,
            UsuarioService usuarioService
    ) {
        this.administradorRepository = administradorRepository;
        this.usuarioService = usuarioService;
    }

    // Cadastra os dados de um administrador.
    @Transactional
    public Administrador cadastrar(
            UUID usuarioId,
            LocalDate dataContratacao
    ) {

        // Confirma que o usuário existe.
        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        // Exige o perfil ADMINISTRADOR.
        if (usuario.getPerfil() != PerfilUsuario.ADMINISTRADOR) {
            throw new IllegalArgumentException(
                    "O usuário não possui perfil ADMINISTRADOR."
            );
        }

        // Impede o cadastro de usuários inativos.
        if (!usuario.isAtivo()) {
            throw new IllegalArgumentException(
                    "O usuário está inativo."
            );
        }

        // Impede registros duplicados.
        if (administradorRepository.existsById(usuarioId)) {
            throw new IllegalStateException(
                    "Este usuário já possui cadastro de administrador."
            );
        }

        // Valida a data de contratação.
        if (dataContratacao == null ||
                dataContratacao.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Data de contratação inválida."
            );
        }

        // Cria e salva o administrador.
        Administrador administrador = new Administrador(
                usuario,
                dataContratacao
        );

        return administradorRepository.save(administrador);
    }
}