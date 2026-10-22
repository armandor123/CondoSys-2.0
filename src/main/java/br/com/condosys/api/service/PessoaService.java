package br.com.condosys.api.service;

import br.com.condosys.api.domain.model.Pessoa;
import br.com.condosys.api.repository.PessoaRepository;
import org.springframework.stereotype.Service;
import br.com.condosys.api.exception.DocumentoJaCadastradoException;
import br.com.condosys.api.exception.PessoaNaoEncontradaException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;



// Regras de negócio relacionadas a Pessoa.
@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;


    // Spring injeta o Repository pelo construtor.
    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }


    // Salva uma nova pessoa, evitando documento duplicado.
    public Pessoa salvar(Pessoa pessoa) {

        if (pessoaRepository.existsByDocumento(pessoa.getDocumento())) {
            throw new DocumentoJaCadastradoException();
        }

        return pessoaRepository.save(pessoa);
    }


    // Lista somente pessoas ativas.
    public List<Pessoa> listarTodas() {
        return pessoaRepository.findAllByAtivoTrue();
    }

    // Busca pelo documento ou informa que a pessoa não existe.
    public Pessoa buscarPorDocumento(String documento) {
        return pessoaRepository.findByDocumento(documento)
                .orElseThrow(PessoaNaoEncontradaException::new);
    }


    // Busca uma pessoa pelo ID ou gera erro se não existir.
    public Pessoa buscarPorId(UUID id) {
        return pessoaRepository.findById(id)
                .orElseThrow(PessoaNaoEncontradaException::new);

    }

    // Atualiza somente os dados permitidos.
    public Pessoa atualizar(
            UUID id,
            String nome,
            String documento,
            String telefone
    ) {

        Pessoa pessoa = buscarPorId(id);

        if (pessoaRepository.existsByDocumentoAndIdNot(documento, id)) {
            throw new DocumentoJaCadastradoException();
        }

        pessoa.setNome(nome);
        pessoa.setDocumento(documento);
        pessoa.setTelefone(telefone);

        return pessoaRepository.save(pessoa);
    }
    // Desativa a pessoa sem apagar o registro do banco.
    public void desativar(UUID id) {

        Pessoa pessoa = buscarPorId(id);

        pessoa.desativar();

        pessoaRepository.save(pessoa);
    }

    // Reativa uma pessoa desativada.
    public Pessoa reativar(UUID id) {

        Pessoa pessoa = buscarPorId(id);

        pessoa.ativar();

        return pessoaRepository.save(pessoa);
    }
}

