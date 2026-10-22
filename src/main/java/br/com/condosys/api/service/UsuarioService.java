package br.com.condosys.api.service;

import br.com.condosys.api.domain.enums.PerfilUsuario;
import br.com.condosys.api.domain.model.Pessoa;
import br.com.condosys.api.domain.model.Usuario;
import br.com.condosys.api.exception.EmailJaCadastradoException;
import br.com.condosys.api.exception.PessoaJaPossuiUsuarioException;
import br.com.condosys.api.exception.UsuarioNaoEncontradoException;
import br.com.condosys.api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.condosys.api.exception.SenhaAtualIncorretaException;


import java.util.List;
import java.util.UUID;


// Regras de negócio relacionadas a Usuario.
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PessoaService pessoaService;
    private final PasswordEncoder passwordEncoder;


    // Spring injeta as dependências pelo construtor.
    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PessoaService pessoaService,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.pessoaService = pessoaService;
        this.passwordEncoder = passwordEncoder;
    }


    // Cria um usuário ligado a uma Pessoa existente.
    public Usuario cadastrar(
            UUID pessoaId,
            String email,
            String senha,
            PerfilUsuario perfil
    ) {

        // Confirma que a Pessoa existe.
        Pessoa pessoa = pessoaService.buscarPorId(pessoaId);


        // Impede dois usuários para a mesma Pessoa.
        if (usuarioRepository.existsByPessoa_Id(pessoaId)) {
            throw new PessoaJaPossuiUsuarioException();
        }


        // Impede e-mail duplicado.
        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailJaCadastradoException();
        }


        // Gera o hash seguro da senha.
        String senhaHash = passwordEncoder.encode(senha);


        // Cria a entidade com o hash, nunca com a senha original.
        Usuario usuario = new Usuario(
                pessoa,
                email,
                senhaHash,
                perfil
        );


        return usuarioRepository.save(usuario);
    }


    // Lista somente usuários ativos.
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAllByAtivoTrue();
    }

    // Reativa um usuário desativado.
    public Usuario reativar(UUID id) {

        Usuario usuario = buscarPorId(id);

        usuario.ativar();

        return usuarioRepository.save(usuario);
    }


    // Busca pelo ID.
    public Usuario buscarPorId(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }

    // Atualiza e-mail e perfil do usuário.
    public Usuario atualizar(
            UUID id,
            String email,
            PerfilUsuario perfil
    ) {

        Usuario usuario = buscarPorId(id);

        if (usuarioRepository.existsByEmailAndIdNot(email, id)) {
            throw new EmailJaCadastradoException();
        }

        usuario.alterarEmail(email);
        usuario.alterarPerfil(perfil);

        return usuarioRepository.save(usuario);
    }

    // Desativa o usuário sem apagar o registro.
    public void desativar(UUID id) {

        Usuario usuario = buscarPorId(id);

        usuario.desativar();

        usuarioRepository.save(usuario);
    }

    // Altera a senha após validar a senha atual.
    public void alterarSenha(
            UUID id,
            String senhaAtual,
            String novaSenha
    ) {

        Usuario usuario = buscarPorId(id);

        // Compara a senha digitada com o hash armazenado.
        if (!passwordEncoder.matches(
                senhaAtual,
                usuario.getSenhaHash()
        )) {
            throw new SenhaAtualIncorretaException();
        }

        // Gera um novo hash para a nova senha.
        String novoHash = passwordEncoder.encode(novaSenha);

        usuario.alterarSenhaHash(novoHash);

        usuarioRepository.save(usuario);
    }
    // Busca usuário pelo e-mail ou retorna erro 404.
    public Usuario buscarPorEmail(String email) {

        return usuarioRepository.findByEmail(email)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }
}