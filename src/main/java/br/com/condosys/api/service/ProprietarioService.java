package br.com.condosys.api.service;

import br.com.condosys.api.domain.enums.PerfilUsuario;
import br.com.condosys.api.domain.model.Proprietario;
import br.com.condosys.api.domain.model.Usuario;
import br.com.condosys.api.exception.ProprietarioNaoEncontradoException;
import br.com.condosys.api.repository.ProprietarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

// Regras de negócio relacionadas aos proprietários.
@Service
public class ProprietarioService {

    private final ProprietarioRepository proprietarioRepository;
    private final UsuarioService usuarioService;

    // Spring injeta as dependências.
    public ProprietarioService(
            ProprietarioRepository proprietarioRepository,
            UsuarioService usuarioService
    ) {
        this.proprietarioRepository = proprietarioRepository;
        this.usuarioService = usuarioService;
    }


    // Cadastra um usuário como proprietário.
    @Transactional
    public Proprietario cadastrar(
            UUID usuarioId,
            LocalDate dataAdesao
    ) {

        // Confirma que o usuário existe.
        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        // Exige perfil PROPRIETARIO.
        if (usuario.getPerfil() != PerfilUsuario.PROPRIETARIO) {
            throw new IllegalArgumentException(
                    "O usuário não possui perfil PROPRIETARIO."
            );
        }

        // Impede cadastro de usuário inativo.
        if (!usuario.isAtivo()) {
            throw new IllegalArgumentException(
                    "O usuário está inativo."
            );
        }

        // Impede cadastro duplicado.
        if (proprietarioRepository.existsById(usuarioId)) {
            throw new IllegalStateException(
                    "Este usuário já possui cadastro de proprietário."
            );
        }

        // Valida a data de adesão.
        if (dataAdesao == null ||
                dataAdesao.isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Data de adesão inválida."
            );
        }

        Proprietario proprietario =
                new Proprietario(
                        usuario,
                        dataAdesao
                );

        return proprietarioRepository.save(proprietario);
    }


    // Lista todos os proprietários cadastrados.
    @Transactional(readOnly = true)
    public List<Proprietario> listarTodos() {

        return proprietarioRepository.findAll();
    }


    // Busca um proprietário pelo UUID do usuário.
    @Transactional(readOnly = true)
    public Proprietario buscarPorId(UUID usuarioId) {

        return proprietarioRepository
                .findById(usuarioId)
                .orElseThrow(
                        ProprietarioNaoEncontradoException::new
                );
    }
}