package br.com.condosys.api.service;

import br.com.condosys.api.domain.enums.PerfilUsuario;
import br.com.condosys.api.domain.model.Porteiro;
import br.com.condosys.api.domain.model.Usuario;
import br.com.condosys.api.exception.PorteiroNaoEncontradoException;
import br.com.condosys.api.repository.PorteiroRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

// Regras de negócio relacionadas aos porteiros.
@Service
public class PorteiroService {

    private final PorteiroRepository porteiroRepository;
    private final UsuarioService usuarioService;

    // Spring injeta as dependências.
    public PorteiroService(
            PorteiroRepository porteiroRepository,
            UsuarioService usuarioService
    ) {
        this.porteiroRepository = porteiroRepository;
        this.usuarioService = usuarioService;
    }


    // Cadastra um usuário como porteiro.
    @Transactional
    public Porteiro cadastrar(
            UUID usuarioId,
            String turno
    ) {

        // Confirma que o usuário existe.
        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        // Exige perfil PORTEIRO.
        if (usuario.getPerfil() != PerfilUsuario.PORTEIRO) {
            throw new IllegalArgumentException(
                    "O usuário não possui perfil PORTEIRO."
            );
        }

        // Impede cadastro de usuário inativo.
        if (!usuario.isAtivo()) {
            throw new IllegalArgumentException(
                    "O usuário está inativo."
            );
        }

        // Impede cadastro duplicado.
        if (porteiroRepository.existsById(usuarioId)) {
            throw new IllegalStateException(
                    "Este usuário já possui cadastro de porteiro."
            );
        }

        // Cria o porteiro.
        Porteiro porteiro = new Porteiro(
                usuario,
                turno
        );

        return porteiroRepository.save(porteiro);
    }


    // Lista todos os porteiros cadastrados.
    @Transactional(readOnly = true)
    public List<Porteiro> listarTodos() {

        return porteiroRepository.findAll();
    }


    // Busca um porteiro pelo UUID do usuário.
    @Transactional(readOnly = true)
    public Porteiro buscarPorId(UUID usuarioId) {

        return porteiroRepository
                .findById(usuarioId)
                .orElseThrow(
                        PorteiroNaoEncontradoException::new
                );
    }


    // Atualiza o turno do porteiro.
    @Transactional
    public Porteiro atualizarTurno(
            UUID usuarioId,
            String novoTurno
    ) {

        Porteiro porteiro = buscarPorId(usuarioId);

        porteiro.atualizarTurno(novoTurno);

        return porteiroRepository.save(porteiro);
    }


    // Registra a demissão do porteiro.
    @Transactional
    public Porteiro registrarDemissao(
            UUID usuarioId,
            LocalDate dataDemissao
    ) {

        Porteiro porteiro = buscarPorId(usuarioId);

        porteiro.registrarDemissao(dataDemissao);

        return porteiroRepository.save(porteiro);
    }
}